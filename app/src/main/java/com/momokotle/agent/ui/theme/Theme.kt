package com.momokotle.agent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MoMoYellow = Color(0xFFFFCC00)
private val MoMoBlack = Color(0xFF1A1A1A)
private val MoMoDarkSurface = Color(0xFF121212)
private val MoMoLightSurface = Color(0xFFF5F5F5)

private val DarkColors = darkColorScheme(
    primary = MoMoYellow,
    onPrimary = MoMoBlack,
    secondary = Color(0xFFFFD633),
    onSecondary = MoMoBlack,
    background = MoMoBlack,
    surface = MoMoDarkSurface,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColors = lightColorScheme(
    primary = MoMoBlack,
    onPrimary = MoMoYellow,
    secondary = MoMoYellow,
    onSecondary = MoMoBlack,
    background = MoMoLightSurface,
    surface = Color.White,
    onBackground = MoMoBlack,
    onSurface = MoMoBlack
)

@Composable
fun MomoKotleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
