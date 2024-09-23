package com.aslansoft.myactivities.classes

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class PrefReminders(
    val date: String,
    val enabled: Boolean,
    val id: Int,
    val note: String,
    val type: String
)

fun getPrefReminders(context: Context): List<PrefReminders> {
    val sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
    val jsonStr = sharedPreferences.getString("reminders", "[]")
    val type = object : TypeToken<List<PrefReminders>>() {

    }.type
    return Gson().fromJson(jsonStr, type)
}