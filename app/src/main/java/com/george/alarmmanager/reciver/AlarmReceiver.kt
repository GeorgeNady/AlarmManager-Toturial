package com.george.alarmmanager.reciver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.george.alarmmanager.R
import com.george.alarmmanager.ui.MainActivity
import com.george.alarmmanager.util.ALARM_NOTIFICATION_CHANNEL_ID
import com.george.alarmmanager.util.NOTIFICATION_ID

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java)
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        notificationManager.createAndDeliverNotification(context)
    }

    private fun NotificationManager.createAndDeliverNotification(ctx: Context) {
        val contentIntent = Intent(ctx, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(ctx, NOTIFICATION_ID, contentIntent, FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(ctx, ALARM_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Stand Up Alert")
            .setContentText("You should stand up and walk around now!")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notify(NOTIFICATION_ID, notificationBuilder.build())
        Log.d("AlarmReceiver", "notify() called")
    }

}