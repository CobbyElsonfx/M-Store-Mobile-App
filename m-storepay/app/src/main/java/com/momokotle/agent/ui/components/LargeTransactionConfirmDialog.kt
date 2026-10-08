package com.momokotle.agent.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.momokotle.agent.domain.ussd.LargeTransactionWarning
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppFormStyles

@Composable
fun LargeTransactionConfirmDialog(
    amount: Double,
    phoneNumber: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppFormStyles.card,
        titleContentColor = AppFormStyles.onSurface,
        textContentColor = AppFormStyles.onSurface,
        title = {
            Text(
                text = "⚠️ LARGE TRANSACTION",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.Warning
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Amount: ${LargeTransactionWarning.formatAmountGhs(amount)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppFormStyles.onSurface
                )
                phoneNumber?.takeIf { it.isNotBlank() }?.let { phone ->
                    Text(
                        text = "Number: $phone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppFormStyles.onSurface
                    )
                }
                Text(
                    text = "Please confirm that the number and amount are correct.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.onSurface
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm & continue", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
