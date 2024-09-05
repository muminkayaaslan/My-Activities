import com.aslansoft.myactivities.Data.ActivityDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.format.DateTimeFormatter

class ReminderRepo(private val activityDao: ActivityDao) {
    suspend fun getReminderForToday(): String?{
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val formattedDate = dateTime.toJavaLocalDateTime().format(formatter)
        return activityDao.getReminderNoteByTypeAndDate("Reminder", formattedDate)
    }
}

fun showReminderNotification(notificationManager: NotificationManager, title: String, message: String) {
    notificationManager.showNotification(title,message)
}

fun chackandshowReminder(notificationManager: NotificationManager,reminderRepo: ReminderRepo){
    GlobalScope.launch(Dispatchers.IO) {
        val note = reminderRepo.getReminderForToday()
        if (!note.isNullOrEmpty()){
            val message = note
            showReminderNotification(notificationManager, title = "Hatırlatma",message = message)
        }
    }
}