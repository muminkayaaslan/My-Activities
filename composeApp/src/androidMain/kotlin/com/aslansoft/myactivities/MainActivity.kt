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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aslansoft.myactivities.classes.*
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
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.FOREGROUND_SERVICE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.FOREGROUND_SERVICE),1)
        }

        //db
        val dao = getDao(this)


        lifecycleScope.launch {
            val nextReminder = getNextReminder(context = this@MainActivity)
            Log.d("RoomDb & Alarm","Bir sonraki alarm için tarih ${nextReminder?.date}")
            nextReminder.let {
                if (it != null) {
                    setAlarm(applicationContext,it)
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
