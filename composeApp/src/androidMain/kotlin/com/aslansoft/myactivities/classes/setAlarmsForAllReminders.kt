package com.aslansoft.myactivities.classes

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.datetime.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("SuspiciousIndentation")
fun setAlarmsForAllReminder(context: Context) {
    val reminders = getPrefReminders(context)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())

    val currentTime = System.currentTimeMillis()
    reminders.forEach { reminder ->
        if (reminder.enabled) {
            val reminderDate = dateFormat.parse(reminder.date)
            reminderDate?.let {
                val reminderTimeInMillis = it.time

                // Geçmiş tarihli hatırlatıcıları atla
                if (reminderTimeInMillis > currentTime) {
                    Log.d("setAlarms", "Setting alarm for reminder ID: ${reminder.id} at $reminderTimeInMillis")
                    setAlarms(context, reminderTimeInMillis, reminder.id, reminder.note)
                } else {
                    Log.d("setAlarms", "Skipping past reminder: ${reminder.date}")
                }
            }
        }
    }
}
