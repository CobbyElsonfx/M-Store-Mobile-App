package com.momokotle.agent.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Central MoMo Kotle palette. Screens should prefer MaterialTheme.colorScheme
 * and AppFormStyles rather than these hex values directly.
 */
object AppColors {
    /** MTN-style yellow used as Material 3 primary in the light theme. */
    val AccentYellow = Color(0xFFFACC15)

    /** Near-black ink for text on yellow and for headings. */
    val PrimaryDark = Color(0xFF141D30)
    val OnAccent = PrimaryDark

    val SecondaryDark = Color(0xFF181C21)
    val BackgroundLight = Color(0xFFF8FAFC)
    val CardLight = Color(0xFFFFFFFF)
    val FormFieldLight = Color(0xFFE2E8F0)
    val FormFieldDark = Color(0xFF243044)
    val FormFieldBorder = Color(0xFF94A3B8)
    val FormInputText = Color(0xFF0F172A)
    val FormPlaceholder = Color(0xFF475569)
    val TextDark = Color(0xFF111827)
    val MutedText = Color(0xFF6B7280)
    val Border = Color(0xFFE5E7EB)

    val Success = Color(0xFF16A34A)
    val SuccessContainer = Color(0xFFDCFCE7)
    val Warning = Color(0xFFF59E0B)
    val WarningContainer = Color(0xFFFEF3C7)
    val Error = Color(0xFFDC2626)
    val ErrorContainer = Color(0xFFFEE2E2)
    val Info = Color(0xFF2563EB)
    val InfoContainer = Color(0xFFDBEAFE)
    val PrimaryContainer = Color(0xFFFEF3C7)
    val Inactive = Color(0xFF9CA3AF)

    val DarkBackground = Color(0xFF141D30)
    val DarkSurface = Color(0xFF181C21)
    val DarkText = Color(0xFFF9FAFB)
    val DarkMutedText = Color(0xFFCBD5E1)
    val DarkBorder = Color(0xFF334155)

    val SurfaceVariantLight = Color(0xFFF1F5F9)
}
