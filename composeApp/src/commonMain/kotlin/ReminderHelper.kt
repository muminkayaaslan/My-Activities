import androidx.room.Dao
import androidx.room.Query
import com.aslansoft.myactivities.Data.ActivityDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.format.DateTimeFormatter
import javax.management.loading.ClassLoaderRepository

class ReminderRepo(private val activityDao: ActivityDao) {
    suspend fun getReminderForToday(): String?{
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")
        val formattedDate = currentDate.toJavaLocalDateTime().format(formatter)
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
            val message = "Hatırlatıcı \n ${note} "
            showReminderNotification(notificationManager, title = "My Activities",message = message)
        }
    }
}