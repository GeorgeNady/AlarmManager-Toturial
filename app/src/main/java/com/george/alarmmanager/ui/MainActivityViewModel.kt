package com.george.alarmmanager.ui

import android.app.Application
import android.app.NotificationManager
import androidx.lifecycle.AndroidViewModel
import com.george.alarmmanager.util.createNotificationChannels

class MainActivityViewModel(private val app: Application): AndroidViewModel(app) {

    private val notificationManager by lazy { app.applicationContext.getSystemService(NotificationManager::class.java) }

    init {
        notificationManager.createNotificationChannels()
    }

}