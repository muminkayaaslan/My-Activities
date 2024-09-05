import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aslansoft.myactivities.R

actual class NotificationManagerImpl(private val context: Context) : NotificationManager {
    actual override fun showNotification(title: String, message: String) {
        val notificationId = 1
        val channelId = "reminder_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminder Notifications",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for reminder notifications"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.my_activity_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}

