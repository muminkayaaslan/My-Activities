package com.aslansoft.myactivities

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

actual fun gestureDetect(onAction: () -> Unit,secondAction:() -> Unit): Modifier {
    return Modifier.pointerInput(Unit){
        detectTapGestures(
            onLongPress = { onAction() },
            onTap = { secondAction()}
        )
    }
}