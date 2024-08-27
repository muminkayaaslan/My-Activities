package com.aslansoft.myactivities

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.aslansoft.myactivities.database.getDao
import myactivities.composeapp.generated.resources.Res
import myactivities.composeapp.generated.resources.my_activity_logo
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(InternalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "My Activities",
        icon = painterResource(Res.drawable.my_activity_logo),
        resizable = true,
        state = WindowState(placement = WindowPlacement.Maximized)
    ) {
        val dao = remember { getDao() }
        App(dao)
    }
}