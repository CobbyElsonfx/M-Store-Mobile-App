package com.momokotle.agent.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.momokotle.agent.models.SavedContact

@Composable
fun PhoneNumberWithSaveRow(
    phoneValue: String,
    onPhoneChange: (String) -> Unit,
    onSaveContactClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AgentTextField(
            value = phoneValue,
            onValueChange = onPhoneChange,
            label = "Phone Number",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        TextButton(
            onClick = onSaveContactClick,
            enabled = enabled && phoneValue.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Contact", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun ContactSearchSuggestions(
    contacts: List<SavedContact>,
    onSelect: (SavedContact) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) return

    AppCard(modifier = modifier) {
        Column {
            contacts.forEach { contact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(contact) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = contact.contactName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = contact.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = contact.network.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
