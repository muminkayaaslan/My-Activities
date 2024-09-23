package com.aslansoft.myactivities.classes

import NotificationManager
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aslansoft.myactivities.R
import com.aslansoft.myactivities.getPlatform
import com.google.gson.Gson
import java.util.*

class NotificationReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val note = intent.getStringExtra("reminder_note") ?: "Bir hatırlatmanız var!"
        val reminderId = intent.getIntExtra("reminderId", 0)
            sendNotification(context,reminderId,note)
    }
    private fun sendNotification(context: Context,reminderId: Int,note: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channelId = "reminder_channel"
        val channelName = "Reminder Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.my_activity_logo)
            .setContentTitle("Hatırlatma")
            .setContentText(note)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)

        notificationManager.notify(reminderId, notificationBuilder.build())

    }
}