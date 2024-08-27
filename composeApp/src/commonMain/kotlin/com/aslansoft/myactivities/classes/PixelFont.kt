package com.aslansoft.myactivities.classes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import myactivities.composeapp.generated.resources.*
import myactivities.composeapp.generated.resources.Res
import myactivities.composeapp.generated.resources.pixelifysans_bold
import myactivities.composeapp.generated.resources.pixelifysans_medium
import myactivities.composeapp.generated.resources.pixelifysans_regular
import org.jetbrains.compose.resources.Font

@Composable
fun PixelFontFamily() = FontFamily(
    Font(Res.font.pixelifysans_medium, FontWeight.Medium),
    Font(Res.font.pixelifysans_bold, FontWeight.Bold),
    Font(Res.font.pixelifysans_regular, FontWeight.Normal),
    Font(Res.font.pixelifysans_semibold, FontWeight.SemiBold)
)