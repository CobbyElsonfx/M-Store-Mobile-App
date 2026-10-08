package com.momokotle.agent.ui.theme

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Form surfaces stay light (white) in both light and dark app themes
 * so inputs remain readable and consistent with paper-style forms.
 */
object AppFormStyles {
    val surface: Color = AppColors.CardLight
    val onSurface: Color = AppColors.TextDark
    val label: Color = AppColors.MutedText
    val border: Color = AppColors.Border
    val primary: Color = AppColors.PrimaryDark
    val success: Color = AppColors.Success
    val error: Color = AppColors.Error
}

@Composable
fun appFormFieldColors(
    showError: Boolean = false,
    showSuccess: Boolean = false
): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = AppFormStyles.onSurface,
    unfocusedTextColor = AppFormStyles.onSurface,
    disabledTextColor = AppFormStyles.onSurface,
    cursorColor = AppFormStyles.primary,
    focusedContainerColor = AppFormStyles.surface,
    unfocusedContainerColor = AppFormStyles.surface,
    disabledContainerColor = AppFormStyles.surface,
    focusedBorderColor = when {
        showError -> AppFormStyles.error
        showSuccess -> AppFormStyles.success
        else -> AppFormStyles.primary
    },
    unfocusedBorderColor = when {
        showError -> AppFormStyles.error
        showSuccess -> AppFormStyles.success
        else -> AppFormStyles.border
    },
    disabledBorderColor = when {
        showError -> AppFormStyles.error
        showSuccess -> AppFormStyles.success
        else -> AppFormStyles.border
    },
    focusedLabelColor = AppFormStyles.primary,
    unfocusedLabelColor = AppFormStyles.label,
    disabledLabelColor = AppFormStyles.label,
    focusedPlaceholderColor = AppFormStyles.label,
    unfocusedPlaceholderColor = AppFormStyles.label,
    disabledPlaceholderColor = AppFormStyles.label,
    errorBorderColor = AppFormStyles.error,
    errorLabelColor = AppFormStyles.error,
    errorCursorColor = AppFormStyles.error,
    errorSupportingTextColor = AppFormStyles.error,
    disabledTrailingIconColor = AppFormStyles.label
)

@Composable
fun appFormCardColors() = CardDefaults.cardColors(
    containerColor = AppFormStyles.surface,
    contentColor = AppFormStyles.onSurface
)
