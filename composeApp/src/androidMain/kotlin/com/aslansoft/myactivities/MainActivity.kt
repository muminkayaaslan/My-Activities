@file:OptIn(ExperimentalPermissionsApi::class)

package com.aslansoft.myactivities

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import androidx.work.*
import com.aslansoft.myactivities.classes.*
import com.aslansoft.myactivities.database.getDao
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setAlarmsForAllReminder(this)
        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            saveReminderWorker::class.java,
            1,
            TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        //pref yazdırma
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork("Save Reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest)
            .result
            .addListener({
                Log.d("Activity Work Manager Test","Work sıraya alındı")
            },Executors.newSingleThreadExecutor())
        //pref silme
        scheduleClearSharedPref(this)
        //db
        val dao = getDao(this)
        //alarm kontrol
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
                    androidx.compose.material3.AlertDialog(containerColor = Color(33, 46, 68, 255),onDismissRequest = { notificationDialog.value = false },
                        title = {Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                            Text("Uyarı", fontSize = 20.sp, fontFamily = PixelFontFamily(), color = Color.White)
                        }},
                        confirmButton = {Button(shape = RoundedCornerShape(9.dp),onClick = {
                            notificationPermission.launchPermissionRequest()
                        }) { Text(text = "İzin Ver", fontFamily = PixelFontFamily()) } },
                        dismissButton = {Button(shape = RoundedCornerShape(9.dp),onClick = {activity.finish()}) { Text("Çıkış", fontFamily = PixelFontFamily()) }},
                        text = { Text("Hatırlatıcı özelliğimizi kullanabilmeniz için bildirimlere izin vermeniz gerekmektedir.", fontFamily = PixelFontFamily(), color = Color.White) }

                    )
                }
            }

            App(dao)

        }
    }
}



fun scheduleClearSharedPref(context: Context){
    val now = java.time.LocalTime.now()
    val endOfDay = java.time.LocalTime.of(23,59)
    val durationUntil = Duration.between(now, endOfDay)
    val constraints = Constraints.Builder()
        .setRequiresDeviceIdle(false)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(false)
        .setRequiresStorageNotLow(false)
        .setRequiresCharging(false)
        .build()

    val delay = if ( durationUntil.isNegative) {
        Duration.ofHours(24).minus(durationUntil.abs())

    }else{
        durationUntil
    }

    val clearPreferencesRequest: WorkRequest = PeriodicWorkRequest.Builder(
        ClearReminderWorker::class.java,
        24,
        TimeUnit.HOURS

    ).setConstraints(constraints)
        .setInitialDelay(delay.toMillis(), TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(clearPreferencesRequest)
}

