package com.example.post34

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager

class Post34Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup(NOTIFICATION_GROUP, NOTIFICATION_GROUP)
        )

        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance).apply {
            description = descriptionText
            group = NOTIFICATION_GROUP
        }

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "notificationChannel"
        const val NOTIFICATION_GROUP = "notificationGroup"
    }
}