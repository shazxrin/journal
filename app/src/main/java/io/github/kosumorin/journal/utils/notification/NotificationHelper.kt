package io.github.kosumorin.journal.utils.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

object NotificationHelper {
    object BackupImport {
        const val BACKUP_IMPORT_CHANNEL_ID = "backup_import"
        const val BACKUP_IMPORT_CHANNEL_NAME = "Backup & Import"
        const val BACKUP_ONGOING_NOTIF_ID = 100
        const val BACKUP_COMPLETED_NOTIF_ID = 101
        const val IMPORT_ONGOING_NOTIF_ID = 102
        const val IMPORT_COMPLETED_NOTIF_ID = 103
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)

        return channelId
    }
}