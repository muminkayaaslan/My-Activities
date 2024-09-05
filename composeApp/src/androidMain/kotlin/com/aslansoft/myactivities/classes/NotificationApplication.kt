package com.aslansoft.myactivities.classes

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.time.format.DateTimeFormatter

class NotificationApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        println("bildirim uygulaması çalışıyor")
        scheduleNotification()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification() {
        val dao = getDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO) {
            val datetime = java.time.LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val formattedDate = datetime.format(formatter)
            val note = dao.getReminderNoteByTypeAndDate("Reminder", formattedDate.toString())
            note?.let {
                // Alarmı kur
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this@NotificationApplication, AlarmReceiver::class.java).apply {
                    putExtra("message", it)  // Mesajı buraya ekliyoruz
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
    }
}
