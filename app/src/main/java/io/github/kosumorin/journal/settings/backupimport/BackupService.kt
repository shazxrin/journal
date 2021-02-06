package io.github.kosumorin.journal.settings.backupimport

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.android.DaggerService
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.json.BackupJSON
import io.github.kosumorin.journal.data.json.EntryJSONAdapter
import io.github.kosumorin.journal.data.repository.EntryRepository
import io.github.kosumorin.journal.feature.MainActivity
import io.github.kosumorin.journal.utils.notification.NotificationHelper
import kotlinx.coroutines.*
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import javax.inject.Inject

class BackupService : DaggerService() {
    @Inject
    lateinit var entryRepository: EntryRepository

    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChannel(
                this,
                NotificationHelper.BackupImport.BACKUP_IMPORT_CHANNEL_ID,
                NotificationHelper.BackupImport.BACKUP_IMPORT_CHANNEL_NAME
            )
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

        startForeground(
            NotificationHelper.BackupImport.BACKUP_ONGOING_NOTIF_ID,
            notificationForeground
        )

        if (intent != null && intent.data != null) {
            serviceScope.launch {
                val fileURI = intent.data!!

                val fileOutputStream = contentResolver.openOutputStream(fileURI)
                val writer = BufferedWriter(OutputStreamWriter(fileOutputStream))
                val moshi = Moshi.Builder()
                    .add(EntryJSONAdapter())
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(BackupJSON::class.java)

                val entries = entryRepository.getAllEntries()
                val backupJSON = BackupJSON(data = entries)

                val json = jsonAdapter.toJson(backupJSON)

                writer.write(json)
                writer.flush()
                writer.close()

            }.invokeOnCompletion {
                stopForeground(true)

                val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                service.notify(
                    NotificationHelper.BackupImport.BACKUP_COMPLETED_NOTIF_ID,
                    notificationCompleted
                )

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