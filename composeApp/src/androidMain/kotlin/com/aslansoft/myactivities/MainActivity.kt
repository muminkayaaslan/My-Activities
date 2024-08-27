package com.aslansoft.myactivities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import com.aslansoft.myactivities.database.getDao
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = getDao(this)
        setContent {
            val controller = rememberSystemUiController()
            controller.setStatusBarColor(Color.Black)
            App(dao)
        }
    }
}

