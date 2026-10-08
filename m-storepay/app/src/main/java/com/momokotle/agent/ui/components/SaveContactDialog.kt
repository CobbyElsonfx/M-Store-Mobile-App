package com.momokotle.agent.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.ui.theme.appFormCheckboxColors

data class SaveContactFormState(
    val contactName: String = "",
    val phoneNumber: String = "",
    val network: ContactNetwork = ContactNetwork.UNKNOWN,
    val isPorted: Boolean = false
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SaveContactDialog(
    formState: SaveContactFormState,
    onContactNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onNetworkChange: (ContactNetwork) -> Unit,
    onPortedChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    errorMessage: String? = null,
    phoneEditable: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppFormStyles.card,
        titleContentColor = AppFormStyles.onSurface,
        textContentColor = AppFormStyles.onSurface,
        title = { Text("Save Contact") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AgentTextField(
                    value = formState.contactName,
                    onValueChange = onContactNameChange,
                    label = "Contact Name"
                )
                AgentTextField(
                    value = formState.phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    label = "Phone Number",
                    enabled = phoneEditable,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = "Network",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppFormStyles.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ContactNetwork.entries.forEach { network ->
                        AppFilterChip(
                            label = network.displayName,
                            selected = formState.network == network,
                            onClick = { onNetworkChange(network) }
                        )
                    }
                }
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = formState.isPorted,
                        onCheckedChange = onPortedChange,
                        colors = appFormCheckboxColors()
                    )
                    Text(
                        text = "Number is ported",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
