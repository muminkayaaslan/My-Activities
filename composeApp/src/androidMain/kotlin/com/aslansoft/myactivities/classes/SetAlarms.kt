package com.aslansoft.myactivities.classes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aslansoft.myactivities.MainActivity
import java.util.*

fun setAlarms(context: Context,reminderTimeInMillis: Long,reminderId: Int, note: String) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("reminder_id",reminderId)
        putExtra("reminder_note",note)

    }
    val pendingIntent = PendingIntent.getBroadcast(context, reminderId, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

    Log.d("AlarmSetup", "Setting alarm for reminderId: $reminderId at $reminderTimeInMillis")
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent
    )


}
