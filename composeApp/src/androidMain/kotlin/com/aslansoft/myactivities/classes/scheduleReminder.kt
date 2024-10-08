package com.aslansoft.myactivities.classes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.database.getDao
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDate
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Alarm kurma fonksiyonu
fun setAlarm(context: Context, reminder: ActivityEntity) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
        putExtra("note", reminder.note)
    }
    if(reminder.id != null){

        val pendingIntent = reminder.id?.let { PendingIntent.getBroadcast(context,
            it,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE) }

        // Hatırlatıcı tarihini milisaniye cinsine çevir
        val date = reminder.date
        val reminderTimeInMillis = LocalDateTime.parse(reminder.date).toJavaLocalDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (pendingIntent != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent)
        }
        println("setAlarm çalıştı $reminderTimeInMillis")
        println("şuan tarih ${System.currentTimeMillis()}")
        println("localdatime olan ${date}")
    }else{
        println("Reminder ID: null")
    }



}

