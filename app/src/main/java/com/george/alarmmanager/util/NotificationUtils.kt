package com.george.alarmmanager.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
private fun alarmNotificationChannel(): NotificationChannel {
    val apply = NotificationChannel(
        ALARM_NOTIFICATION_CHANNEL_ID,
        ALARM_NOTIFICATION_CHANNEL_NAME,
        IMPORTANCE_HIGH
    ).apply {
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)
    }
    return apply
}

@RequiresApi(Build.VERSION_CODES.O)
private fun chatNotificationChannel() =
    NotificationChannel(
        CHAT_NOTIFICATION_CHANNEL_ID,
        CHAT_NOTIFICATION_CHANNEL_NAME,
        IMPORTANCE_HIGH
    ).apply {
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)
    }

fun NotificationManager.createNotificationChannels() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channels = listOf(alarmNotificationChannel(), chatNotificationChannel())
        createNotificationChannels(channels)
    }
}