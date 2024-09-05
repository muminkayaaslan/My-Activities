package com.aslansoft.myactivities.classes

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*

class NotificationApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        println("bildirim uygulaması çalışıyor")
        scheduleNotification()
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification() {

            val datetime = java.time.LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val formattedDate = datetime.format(formatter)
        var note: String? = null
            CoroutineScope(Dispatchers.IO).launch {
                note = getDao(applicationContext).getReminderNoteByTypeAndDate("Reminder",formattedDate)
            }
                // Alarmı kur
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this@NotificationApplication, AlarmReceiver::class.java).apply {
                    putExtra("message", note)  // Mesajı buraya ekliyoruz
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    this@NotificationApplication,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Alarmı 1 dakika sonrasına kuruyoruz örnek olarak
                val triggerTime = Calendar.getInstance().apply {
                    add(Calendar.SECOND, 10)
                }.timeInMillis

                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )


    }
}
