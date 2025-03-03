package com.aslansoft.myactivities.classes

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import myactivities.composeapp.generated.resources.*
import myactivities.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


@Composable
fun Economica() = FontFamily(

    Font(
        resource = Res.font.economica_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        resource = Res.font.economica_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resource = Res.font.economica_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resource = Res.font.economica_boldItalic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )

)

@Composable
fun Moderna() = FontFamily(
    Font(
        resource = Res.font.moderna_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resource = Res.font.moderna_thin,
        weight = FontWeight.Thin,
        style = FontStyle.Normal
    ),
    Font(resource = Res.font.moderna_light,
        weight = FontWeight.Light,
        style = FontStyle.Normal),
    Font(resource = Res.font.moderna_extralight,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Normal)
)

@Composable
fun Poppins() = FontFamily(
    Font(resource = Res.font.poppins_black,
        weight = FontWeight.Black,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_black_italic,
        weight = FontWeight.Black,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_extrabold,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_extrabold_italic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_extralight,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_extralight_italic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_light,
        weight = FontWeight.Light,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_light_italic,
        weight = FontWeight.Light,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_medium_italic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_semibold,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_semibold_italic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic),
    Font(resource = Res.font.poppins_thin,
        weight = FontWeight.Thin,
        style = FontStyle.Normal),
    Font(resource = Res.font.poppins_thin_italic,
        weight = FontWeight.Thin,
        style = FontStyle.Italic),
)