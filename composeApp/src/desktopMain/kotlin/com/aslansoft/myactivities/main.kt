package com.aslansoft.myactivities

import NotificationManagerImpl
import ReminderRepo
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.*
import chackandshowReminder
import com.aslansoft.myactivities.database.getDao
import myactivities.composeapp.generated.resources.Res
import myactivities.composeapp.generated.resources.my_activity_logo
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

@OptIn(InternalResourceApi::class)
fun main() = application {
    val dao =  getDao()
    val isOpen = remember { mutableStateOf(true) }
    val repository = ReminderRepo(dao)
    val notificationManager = NotificationManagerImpl()
    Timer().schedule(0,60000){
        chackandshowReminder(notificationManager, repository)
    }

    Tray(

        icon = painterResource(Res.drawable.my_activity_logo),
        menu = {
            Item("Open Window", onClick = { isOpen.value = true })
            Item("Exit", onClick = { exitProcess(1) })

        }
    )
    if (isOpen.value) {
        Window(
            onCloseRequest =   { isOpen.value = false },
            title = "My Activities",
            icon = painterResource(Res.drawable.my_activity_logo),
            resizable = true,
            state = WindowState(placement = WindowPlacement.Maximized)
        ) {


            App(dao)
        }
    }

}

