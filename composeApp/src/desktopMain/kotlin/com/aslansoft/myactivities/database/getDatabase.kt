package com.aslansoft.myactivities.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityDatabase
import java.io.File

fun getDao(): ActivityDao {
    val dbFile = File(System.getProperty("java.io.tmpdir"),"activities.db")
    println("Database file location: ${dbFile.absolutePath}")

    return Room.databaseBuilder<ActivityDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
        .getDao()
}