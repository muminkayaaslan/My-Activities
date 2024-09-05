package com.aslansoft.myactivities.classes

import NotificationManagerImpl
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm tetiklendi!")
        val notificationMessage = intent.getStringExtra("message") ?: null

        val notificationManager = NotificationManagerImpl(context)
        if (notificationMessage != null) {
            notificationManager.showNotification("Hatırlatıcı", notificationMessage)
        }
    }
}