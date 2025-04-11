package com.example.mam.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

//private val DarkColorScheme = darkColorScheme(
//    primary = OrangeDefault,
//    secondary = BrownDefault,
//    tertiary = Momo,
//    background = BlackDefault,
//    surface = BrownDark,
//    onPrimary = WhiteDefault,
//    onSecondary = WhiteDefault,
//    onBackground = GreyDefault,
//    onSurface = WhiteDefault
//)
//private val LightColorScheme = lightColorScheme(
//    primary = OrangeDefault,
//    secondary = OrangeLight,
//    tertiary = BrownLight,
//    background = WhiteDefault,
//    surface = GreyDefault,
//    onPrimary = BlackDefault,
//    onSecondary = BlackDefault,
//    onBackground = BlackDefault,
//    onSurface = BlackDefault
//)

object Variables {
    val HeadlineMediumSize = 28.sp
    val HeadlineMediumLineHeight = 36.sp
    val BodySizeMedium = 16.sp
    val BodyFontWeightRegular = 400
}

@Composable
fun MAMTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    //dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        //colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
