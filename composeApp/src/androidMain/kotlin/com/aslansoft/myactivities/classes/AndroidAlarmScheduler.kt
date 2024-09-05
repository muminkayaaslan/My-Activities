package com.aslansoft.myactivities.classes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.aslansoft.myactivities.MainActivity
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class AndroidAlarmScheduler (private val context: Context): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("Extra message",item.message)
        }
        val localDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")
        val formattedDate = localDateTime.toJavaLocalDateTime().format(formatter)
        val db = getDao(context)
        GlobalScope.launch(Dispatchers.IO) {
            db.getReminderNoteByTypeAndDate("Reminder",formattedDate)
        }
        val alarmTimeInMillis = item.time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeInMillis,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            Intent(context, MainActivity::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        ))

    }
}