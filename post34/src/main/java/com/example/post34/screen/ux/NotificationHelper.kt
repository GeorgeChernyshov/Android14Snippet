package com.example.post34.screen.ux

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.post34.Post34Application
import com.example.post34.R

class NotificationHelper(private val context: Context) {

    fun showNotification() {
        val builder = NotificationCompat
            .Builder(context, Post34Application.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setNumber(10)
            .setOngoing(true)
            .setTimeoutAfter(10000)
            .setColor(context.resources.getColor(R.color.purple_200))

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notify(notificationId, builder.build())
        }
    }

    companion object {
        var notificationId = 0
    }
}