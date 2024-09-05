package com.aslansoft.myactivities.classes

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import com.aslansoft.myactivities.Data.ActivityDatabase
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.format.DateTimeFormatter

class NotificationServices: IntentService("AlarmIntentService") {
    private lateinit var alarmScheduler: AndroidAlarmScheduler

    override fun onCreate() {
        super.onCreate()
        alarmScheduler = AndroidAlarmScheduler(this)
    }
    override fun onHandleIntent(intent: Intent?) {
        val alarmScheduler = AndroidAlarmScheduler(this)
        runBlocking {
            val dao = getDao(context = applicationContext)
            val currentDate = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            val reminderNote = dao.getReminderNoteByTypeAndDate("Reminder",currentDate.toString())
            if (reminderNote != null) {
                val alarmItem = AlarmItem(
                    message = reminderNote,
                    time = java.time.LocalDateTime.now()
                )
                alarmScheduler.schedule(alarmItem)
            }

        }
    }
}