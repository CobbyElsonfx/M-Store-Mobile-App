package com.momokotle.agent.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.momokotle.agent.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSelectField(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    errorText: String?,
    isValid: Boolean,
    showValidation: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val showError = showValidation && !errorText.isNullOrBlank()
    val showSuccess = showValidation && isValid && errorText.isNullOrBlank() && value.isNotBlank()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            isError = showError,
            supportingText = if (showError) {
                { Text(errorText.orEmpty(), color = MaterialTheme.colorScheme.error) }
            } else {
                null
            },
            trailingIcon = {
                when {
                    showSuccess -> Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Valid",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    else -> ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(AppDimens.InputRadius),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when {
                    showError -> MaterialTheme.colorScheme.error
                    showSuccess -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.primary
                },
                unfocusedBorderColor = when {
                    showError -> MaterialTheme.colorScheme.error
                    showSuccess -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.outline
                },
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
