@file:OptIn(ExperimentalPermissionsApi::class)

package com.aslansoft.myactivities

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aslansoft.myactivities.classes.getNextReminder
import com.aslansoft.myactivities.classes.setAlarm
import com.aslansoft.myactivities.database.getDao
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.FOREGROUND_SERVICE), 1
            )
        }

        //db
        val dao = getDao(this)


        lifecycleScope.launch {
            val nextReminder = getNextReminder(context = this@MainActivity)
            Log.d("RoomDb & Alarm", "Bir sonraki alarm için tarih ${nextReminder?.date}")
            nextReminder.let {
                if (it != null) {
                    setAlarm(applicationContext, it)
                }
            }
        }

        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Color.Black)
            val notificationPermission =
                rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
            LaunchedEffect(notificationPermission) {
                if (!notificationPermission.status.isGranted) {

                    notificationPermission.launchPermissionRequest()

                }
            }

            App(dao)


        }
    }
}
