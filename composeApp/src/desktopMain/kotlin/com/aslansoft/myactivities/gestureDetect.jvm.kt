package com.aslansoft.myactivities

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.pointerInput

@ExperimentalFoundationApi
actual fun gestureDetect(onAction: () -> Unit,secondAction: () -> Unit): Modifier{
    return Modifier.pointerInput(Unit){
        detectTapGestures(
            onTap = {
                onAction()
            }
            , matcher = PointerMatcher.mouse(PointerButton.Secondary)
        )
    }
}