@file:OptIn(ExperimentalPermissionsApi::class)

package com.aslansoft.myactivities

import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aslansoft.myactivities.classes.PixelFontFamily
import com.aslansoft.myactivities.classes.ReminderWorker
import com.aslansoft.myactivities.database.getDao
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = getDao(this)
        scheduleReminderCheck(this)

        /*
        var noteValue: String?
        lifecycleScope.launchWhenCreated {
            val datetime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val formattedDate = datetime.format(formatter)
            noteValue = dao.getReminderNoteByTypeAndDate("Reminder",formattedDate.toString())

            noteValue?.let {
                val timer = LocalDateTime.now()
                val scheduler = AndroidAlarmScheduler(this@MainActivity)
                val alarmItem = AlarmItem(timer, noteValue!!)
                scheduler.schedule(alarmItem)
            }

        }

*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(this, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    this@MainActivity.startActivity(intent)
                }
            }
        }


        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Color.Black)
            val  notificationDialog = remember{ mutableStateOf(false) }
            val notificationPermission = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
            notificationDialog.value = !notificationPermission.status.isGranted
            if (!notificationPermission.status.isGranted){
                if (notificationDialog.value){
                    val activity = this@MainActivity
                    androidx.compose.material3.AlertDialog(onDismissRequest = { notificationDialog.value = false },
                        title = {Row(modifier = Modifier.fillMaxWidth().height(40.dp),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                            Text("Uyarı", fontSize = 20.sp, fontFamily = PixelFontFamily())
                        }},
                        confirmButton = {Button(shape = RoundedCornerShape(9.dp),onClick = {
                            notificationPermission.launchPermissionRequest()
                        }) { Text(text = "İzin Ver", fontFamily = PixelFontFamily()) } },
                        dismissButton = {Button(shape = RoundedCornerShape(9.dp),onClick = {activity.finish()}) { Text("Çıkış", fontFamily = PixelFontFamily()) }},
                        text = { Text("Hatırlatıcı özelliğimizi kullanabilmeniz için bildirimlere izin vermeniz gerekmektedir.", fontFamily = PixelFontFamily()) }

                    )
                }
            }
                App(dao)
        }
    }
}



fun scheduleReminderCheck(context: Context) {
    println("worker çalıştı")
    val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "ReminderCheckWork",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
