package com.momokotle.agent.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AppColors.AccentYellow,
    onPrimary = AppColors.OnAccent,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.OnAccent,
    secondary = AppColors.AccentYellow,
    onSecondary = AppColors.OnAccent,
    secondaryContainer = AppColors.PrimaryContainer,
    onSecondaryContainer = AppColors.OnAccent,
    tertiary = AppColors.Info,
    onTertiary = AppColors.CardLight,
    tertiaryContainer = AppColors.InfoContainer,
    onTertiaryContainer = AppColors.Info,
    background = AppColors.BackgroundLight,
    onBackground = AppColors.TextDark,
    surface = AppColors.CardLight,
    onSurface = AppColors.TextDark,
    surfaceVariant = AppColors.SurfaceVariantLight,
    onSurfaceVariant = AppColors.MutedText,
    outline = AppColors.Border,
    outlineVariant = AppColors.Border,
    error = AppColors.Error,
    onError = AppColors.CardLight,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.Error
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
    tertiary = AppColors.Info,
    onTertiary = AppColors.DarkText,
    tertiaryContainer = AppColors.SecondaryDark,
    onTertiaryContainer = AppColors.DarkText,
    background = AppColors.DarkBackground,
    onBackground = AppColors.DarkText,
    surface = AppColors.DarkSurface,
    onSurface = AppColors.DarkText,
    surfaceVariant = AppColors.DarkBackground,
    onSurfaceVariant = AppColors.DarkMutedText,
    outline = AppColors.DarkBorder,
    outlineVariant = AppColors.DarkBorder,
    error = AppColors.Error,
    onError = AppColors.DarkText,
    errorContainer = AppColors.SecondaryDark,
    onErrorContainer = AppColors.Error
)

@Composable
fun MomoKotleTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
