package io.github.icedshytea.journal.feature.settings

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.android.DaggerService
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.data.json.BackupJSON
import io.github.icedshytea.journal.data.json.EntryJSONAdapter
import io.github.icedshytea.journal.data.repository.EntryRepository
import io.github.icedshytea.journal.feature.MainActivity
import io.github.icedshytea.journal.utils.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ImportService : DaggerService() {
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
            .setContentTitle("Importing backup")
            .setContentText("Importing entries from JSON backup.")
            .setSmallIcon(R.drawable.ic_backup_import)
            .setContentIntent(pendingIntent)
            .setTicker("Importing backup")
            .build()

        val notificationCompleted: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Import successful")
            .setContentText("Backup successful imported.")
            .setSmallIcon(R.drawable.ic_backup_import)
            .setTicker("Import successful")
            .build()

        startForeground(
            NotificationHelper.BackupImport.IMPORT_ONGOING_NOTIF_ID,
            notificationForeground
        )

        if (intent != null && intent.data != null) {
            serviceScope.launch {
                val fileURI = intent.data!!

                val fileOutputStream = contentResolver.openInputStream(fileURI)
                val reader = BufferedReader(InputStreamReader(fileOutputStream))
                val moshi = Moshi.Builder()
                    .add(EntryJSONAdapter())
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi.adapter(BackupJSON::class.java)

                val json = reader.readText()

                val backupJSON = jsonAdapter.fromJson(json)
                reader.close()

                if (backupJSON != null) {
                    for (e in backupJSON.data) {
                        entryRepository.insert(e)
                    }
                }
            }.invokeOnCompletion {
                stopForeground(true)

                val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                service.notify(
                    NotificationHelper.BackupImport.IMPORT_COMPLETED_NOTIF_ID,
                    notificationCompleted
                )

                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}