package com.aslansoft.myactivities.classes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoStart : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // AlarmManager kullanarak bir alarm ayarla
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentService = Intent(context, ReminderBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intentService, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            // Alarmı bir zaman dilimine ayarlayın (örneğin her 1 saatte bir kontrol et)
            val interval = AlarmManager.INTERVAL_HOUR // Her saat
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                interval,
                pendingIntent
            )
        }
    }
}

