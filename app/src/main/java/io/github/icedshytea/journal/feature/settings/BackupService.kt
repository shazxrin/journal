package io.github.icedshytea.journal.feature.settings

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.android.DaggerService
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.data.entity.Entry
import io.github.icedshytea.journal.data.json.EntryJSONAdapter
import io.github.icedshytea.journal.data.repository.EntryRepository
import io.github.icedshytea.journal.feature.MainActivity
import kotlinx.coroutines.*
import org.threeten.bp.format.DateTimeFormatter
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import javax.inject.Inject

class BackupService : DaggerService() {
    @Inject
    lateinit var entryRepository: EntryRepository

    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    private val ONGOING_NOTIFICATION_ID = 1
    private val COMPLETED_NOTIFICATION_ID = 2

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)

        return channelId
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("backup_restore", "Backup & Restore")
        } else {
            ""
        }

        val notificationForeground: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Backing up")
            .setContentText("Exporting entries to JSON.")
            .setSmallIcon(R.drawable.ic_backup_import)
            .setContentIntent(pendingIntent)
            .setTicker("Backing up")
            .build()

        val notificationCompleted: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Backup successful")
            .setContentText("Entries successful exported.")
            .setSmallIcon(R.drawable.ic_backup_import)
            .setTicker("Backup successful")
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notificationForeground)

        if (intent != null && intent.data != null) {
            serviceScope.launch {
                val fileURI = intent.data!!

                val fileOutputStream = contentResolver.openOutputStream(fileURI)
                val writer = BufferedWriter(OutputStreamWriter(fileOutputStream))

                val totalEntries = entryRepository.getEntriesCount()
                val MAX_LIMIT = 5
                var limit = MAX_LIMIT
                var offset = 0
                var remainingEntries = totalEntries

                writer.write("{\"data\":[")
                writer.flush()

                val moshi = Moshi.Builder()
                    .add(EntryJSONAdapter())
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter<Entry>(Entry::class.java)

                var isFirstEntry = true

                while (true) {
                    if (remainingEntries <= 0) {
                        break
                    }

                    val entries = entryRepository.getAllEntries(limit, offset)

                    for (e in entries) {
                        val entryJSON = jsonAdapter.toJson(e)

                        if (isFirstEntry) {
                            writer.write(entryJSON)
                            isFirstEntry = false
                        } else {
                            writer.write(",$entryJSON")
                        }

                        writer.flush()
                    }

                    remainingEntries -= limit
                    offset += limit
                    if (remainingEntries < MAX_LIMIT) {
                        limit = remainingEntries % 5
                    }
                }

                writer.write("]}")
                writer.flush()

                writer.close()
            }.invokeOnCompletion {
                stopForeground(true)

                val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                service.notify(COMPLETED_NOTIFICATION_ID, notificationCompleted)

                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}