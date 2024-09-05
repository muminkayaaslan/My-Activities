package com.aslansoft.myactivities.classes

import NotificationManagerImpl
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aslansoft.myactivities.database.getDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class ReminderWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val dao = getDao(applicationContext)

                val datetime = java.time.LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                val formattedDate = datetime.format(formatter)
                val reminders = dao.getReminderNoteByTypeAndDate("Reminder", formattedDate)

                reminders?.let { notes ->
                    if (notes.isNotEmpty()) {
                        val notificationManager = NotificationManagerImpl(applicationContext)
                        notificationManager.showNotification("Hatırlatma", notes)
                    }
                }

                Result.success()
            } catch (e: Exception) {
                // Hata durumunu loglayabilirsiniz
                println(e.message)
                Result.failure()
            }
        }
    }
}