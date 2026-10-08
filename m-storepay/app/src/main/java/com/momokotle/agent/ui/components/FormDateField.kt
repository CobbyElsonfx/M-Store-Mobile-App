package com.momokotle.agent.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.ui.theme.appFormFieldColors
import com.momokotle.agent.ui.theme.formInputTextStyle
import com.momokotle.agent.utils.FormValidators
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateField(
    label: String,
    value: String,
    placeholder: String,
    errorText: String?,
    isValid: Boolean,
    showValidation: Boolean,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    constrainToAdultDob: Boolean = true
) {
    var showPicker by remember { mutableStateOf(false) }
    val showError = showValidation && !errorText.isNullOrBlank()
    val showSuccess = showValidation && isValid && errorText.isNullOrBlank() && value.isNotBlank()
    val displayValue = value.ifBlank { "" }
    val fieldColors = appFormFieldColors(showError = showError, showSuccess = showSuccess)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showPicker = true }
    ) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            textStyle = formInputTextStyle(),
            label = {
                Text(label, style = MaterialTheme.typography.bodyMedium, color = AppFormStyles.label)
            },
            placeholder = {
                Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = AppFormStyles.label)
            },
            isError = showError,
            supportingText = when {
                showError -> {
                    { Text(errorText.orEmpty(), color = AppFormStyles.error) }
                }
                displayValue.isBlank() -> {
                    { Text("Tap to choose a date (DD/MM/YYYY)", color = AppFormStyles.label) }
                }
                else -> null
            },
            trailingIcon = {
                when {
                    showSuccess -> Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Valid",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    else -> Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = AppFormStyles.primary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimens.InputRadius),
            colors = fieldColors
        )
    }

    if (showPicker) {
        val today = LocalDate.now()
        val maxDate = if (constrainToAdultDob) today.minusYears(18) else today
        val minDate = today.minusYears(100)
        val initialMillis = FormValidators.parseDate(value)
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
            ?: maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !date.isBefore(minDate) && !date.isAfter(maxDate)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(FormValidators.formatDate(date))
                        }
                        showPicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
