package com.aslansoft.myactivities.classes

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aslansoft.myactivities.R
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val note = intent.getStringExtra("note")
        showNotification(context, note ?: "Hatırlatıcı")
        Log.i("ReminderReceiver","onReceive is running")
    }

    private fun showNotification(context: Context, note: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Hatırlatma")
            .setContentText(note)
            .setSmallIcon(R.drawable.my_activity_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}

