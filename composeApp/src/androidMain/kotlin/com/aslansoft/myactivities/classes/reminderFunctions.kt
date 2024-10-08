package com.aslansoft.myactivities.classes

import android.content.Context
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityEntity
import com.aslansoft.myactivities.database.getDao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


suspend fun getNextReminder(context: Context): ActivityEntity? {
    val db = getDao(context)
    val currentDate = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val formattedDate = currentDate.format(formatter).toString()
    return db.getNextReminder(formattedDate)
}

