package com.aslansoft.myactivities.database
import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.aslansoft.myactivities.Data.ActivityDao
import com.aslansoft.myactivities.Data.ActivityDatabase

fun getDao(context: Context): ActivityDao {
    val dbfile = context.getDatabasePath("activities.db")
    return Room.databaseBuilder<ActivityDatabase>(
        context,
        dbfile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
        .getDao()
}