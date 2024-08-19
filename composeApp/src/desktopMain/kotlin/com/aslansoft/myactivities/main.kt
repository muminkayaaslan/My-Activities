package com.aslansoft.myactivities

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import myactivities.composeapp.generated.resources.Res
import myactivities.composeapp.generated.resources.app_logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource

@OptIn(InternalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "My Activities",
        icon = painterResource(Res.drawable.app_logo),
        resizable = true,
        state = WindowState(placement = WindowPlacement.Maximized)
    ) {
        App()
    }
}