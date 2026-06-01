package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealAccent,
    secondary = TealMedium,
    tertiary = WarningAmber,
    background = SlateDarkBackground,
    surface = SlateDarkSurface,
    onPrimary = SlateDarkBackground,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0ECEB),
    onSurface = Color(0xFFE0ECEB)
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    secondary = TealMedium,
    tertiary = WarningAmber,
    background = SlateLightBackground,
    surface = SlateLightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1E2E2C),
    onSurface = Color(0xFF1E2E2C)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamicColor false by default to showcase our gorgeous custom palette!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
