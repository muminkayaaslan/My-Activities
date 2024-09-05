package com.aslansoft.myactivities.classes

import NotificationManagerImpl
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("alıcı açıldı")
        Log.d("AlarmReceiver", "Alarm tetiklendi!")
        // Bildirimi gösterme
        val notificationManager = NotificationManagerImpl(context)
        val notificationMessage = intent.getStringExtra("message") ?: "Hatırlatıcı"
        notificationManager.showNotification("Hatırlatıcı", notificationMessage)
    }
}