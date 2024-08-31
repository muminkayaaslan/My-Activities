package com.aslansoft.myactivities

import androidx.compose.ui.Modifier

expect fun gestureDetect(onAction: ()->Unit,secondAction: () -> Unit) : Modifier