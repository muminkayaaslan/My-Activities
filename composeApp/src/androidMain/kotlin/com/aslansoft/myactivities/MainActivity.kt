package com.aslansoft.myactivities

import NotificationManagerImpl
import ReminderRepo
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import chackandshowReminder
import com.aslansoft.myactivities.database.getDao
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.security.Permissions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = getDao(this)
        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Color.Black)
            chackandshowReminder(notificationManager = NotificationManagerImpl(this), reminderRepo = ReminderRepo(dao))

            App(dao)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AndroidNotificationPermissionDialog(modifier: Modifier = Modifier) {
    val permissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    val activity: MainActivity = MainActivity()
    Dialog(onDismissRequest = {}) {
        if (!permissionState.status.isGranted) {
            Card(modifier = modifier.fillMaxSize(), elevation = 8.dp, backgroundColor = Color(57, 26, 120), contentColor = Color.White) {
                Column(modifier.fillMaxSize()){
                    Row {
                        Text("Dikkat")
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
                    }
                    Text("Hatırlatıcı özelliğimizi kullanabilmek için bildirim izni vermeniz gerekmektedir")
                   Row {
                       Button(onClick = {

                       }){ Text("İzin Ver") }
                       Button(onClick = {
                           activity.finish()
                           System.exit(0)
                       }){ Text("İzin Verme") }
                   }
                }
            }
        }
    }
}

