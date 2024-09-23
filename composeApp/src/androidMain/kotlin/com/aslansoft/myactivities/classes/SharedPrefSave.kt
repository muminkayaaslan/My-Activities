package com.aslansoft.myactivities.classes

import android.content.Context
import com.aslansoft.myactivities.database.getDao
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

@OptIn(DelicateCoroutinesApi::class)
fun saveRemindersToPrefs(context: Context) {
    val sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val localDate = LocalDate.now()
    val gson = Gson()
    GlobalScope.launch {
        val reminders = getDao(context).getReminderbyLocalDate(localDate.toString(),"Reminder")
        val json = gson.toJson(reminders)
        editor.putString("reminders", json)

        editor.apply()
    }
}

fun clearRemindersToPrefs(context: Context) {
    val sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}