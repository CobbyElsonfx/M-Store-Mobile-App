package com.momokotle.agent.ui.theme

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Daylight light-theme tokens. Login, signup, selects, dates, and
 * application fields all share these colors.
 */
object AppFormStyles {
    val card: Color = AppColors.CardLight
    val surface: Color = AppColors.FormFieldLight
    val onSurface: Color = AppColors.FormInputText
    val label: Color = AppColors.FormPlaceholder
    val border: Color = AppColors.FormFieldBorder
    val primary: Color = AppColors.AccentYellow
    val onPrimary: Color = AppColors.OnAccent
    val success: Color = AppColors.Success
    val error: Color = AppColors.Error
    val info: Color = AppColors.Info
}

@Composable
fun formInputTextStyle(): TextStyle = MaterialTheme.typography.bodyLarge.copy(
    color = AppFormStyles.onSurface,
    fontWeight = FontWeight.Medium
)

@Composable
fun appFormFieldColors(
    showError: Boolean = false,
    showSuccess: Boolean = false
): TextFieldColors {
    val text = AppFormStyles.onSurface
    val well = AppFormStyles.surface
    val hint = AppFormStyles.label
    val stroke = when {
        showError -> AppFormStyles.error
        showSuccess -> AppFormStyles.success
        else -> AppFormStyles.border
    }
    val focusStroke = when {
        showError -> AppFormStyles.error
        showSuccess -> AppFormStyles.success
        else -> AppFormStyles.primary
    }
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = text,
        unfocusedTextColor = text,
        disabledTextColor = text,
        errorTextColor = text,
        cursorColor = AppFormStyles.primary,
        focusedContainerColor = well,
        unfocusedContainerColor = well,
        disabledContainerColor = well,
        errorContainerColor = well,
        focusedBorderColor = focusStroke,
        unfocusedBorderColor = stroke,
        disabledBorderColor = stroke,
        errorBorderColor = AppFormStyles.error,
        focusedLabelColor = text,
        unfocusedLabelColor = hint,
        disabledLabelColor = hint,
        errorLabelColor = AppFormStyles.error,
        focusedPlaceholderColor = hint,
        unfocusedPlaceholderColor = hint,
        disabledPlaceholderColor = hint,
        errorPlaceholderColor = hint,
        focusedTrailingIconColor = AppFormStyles.primary,
        unfocusedTrailingIconColor = hint,
        disabledTrailingIconColor = hint,
        errorTrailingIconColor = AppFormStyles.error,
        focusedLeadingIconColor = hint,
        unfocusedLeadingIconColor = hint,
        disabledLeadingIconColor = hint,
        errorLeadingIconColor = hint,
        errorCursorColor = AppFormStyles.error,
        errorSupportingTextColor = AppFormStyles.error
    )
}

@Composable
fun appFormCardColors() = CardDefaults.cardColors(
    containerColor = AppFormStyles.card,
    contentColor = AppFormStyles.onSurface
)

@Composable
fun appFormCheckboxColors() = CheckboxDefaults.colors(
    checkedColor = AppFormStyles.primary,
    uncheckedColor = AppFormStyles.border,
    checkmarkColor = AppFormStyles.onPrimary
)
