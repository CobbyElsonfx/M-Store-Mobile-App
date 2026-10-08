package com.momokotle.agent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryDark,
    onPrimary = AppColors.CardLight,
    primaryContainer = AppColors.PrimaryDark,
    onPrimaryContainer = AppColors.CardLight,
    secondary = AppColors.AccentYellow,
    onSecondary = AppColors.OnAccent,
    secondaryContainer = AppColors.AccentYellow,
    onSecondaryContainer = AppColors.OnAccent,
    tertiary = AppColors.SecondaryDark,
    onTertiary = AppColors.CardLight,
    background = AppColors.BackgroundLight,
    onBackground = AppColors.TextDark,
    surface = AppColors.CardLight,
    onSurface = AppColors.TextDark,
    surfaceVariant = AppColors.SurfaceVariantLight,
    onSurfaceVariant = AppColors.MutedText,
    outline = AppColors.Border,
    outlineVariant = AppColors.Border,
    error = AppColors.Error,
    onError = AppColors.CardLight
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.AccentYellow,
    onPrimary = AppColors.OnAccent,
    primaryContainer = AppColors.SecondaryDark,
    onPrimaryContainer = AppColors.DarkText,
    secondary = AppColors.AccentYellow,
    onSecondary = AppColors.OnAccent,
    secondaryContainer = AppColors.SecondaryDark,
    onSecondaryContainer = AppColors.DarkText,
    tertiary = AppColors.DarkSurface,
    onTertiary = AppColors.DarkText,
    background = AppColors.DarkBackground,
    onBackground = AppColors.DarkText,
    surface = AppColors.DarkSurface,
    onSurface = AppColors.DarkText,
    surfaceVariant = AppColors.DarkBackground,
    onSurfaceVariant = AppColors.DarkMutedText,
    outline = AppColors.DarkBorder,
    outlineVariant = AppColors.DarkBorder,
    error = AppColors.Error,
    onError = AppColors.DarkText
)

@Composable
fun MomoKotleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
