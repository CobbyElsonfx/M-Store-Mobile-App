package com.momokotle.agent.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.presentation.transactions.TransactionsViewModel
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.utils.PhoneNumberFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun DuplicateTransactionWarningDialog(
    prior: Transaction,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(prior.id) {
        vibrateStrongWarning(context)
    }

    val phone = PhoneNumberFormatter.normalizeGhanaNumber(prior.phoneNumber.orEmpty())
        .ifBlank { prior.phoneNumber.orEmpty() }
    val typeLabel = TransactionsViewModel.typeLabel(prior.type)
    val amountLabel = if (prior.amount > 0.0) {
        String.format(Locale.US, "GHS %,.2f", prior.amount)
    } else {
        "No amount"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppFormStyles.card,
        titleContentColor = AppFormStyles.onSurface,
        textContentColor = AppFormStyles.onSurface,
        title = {
            Text(
                text = "⚠️ DUPLICATE TRANSACTION",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.Error
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "This number was used recently.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppFormStyles.onSurface
                )
                Text(
                    text = "Number: $phone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.onSurface
                )
                Text(
                    text = "$typeLabel · $amountLabel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.onSurface
                )
                Text(
                    text = relativeTimeAgo(prior.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Please confirm this is intentional before continuing.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.onSurface
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Continue anyway", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun relativeTimeAgo(timestampMs: Long): String {
    val elapsed = (System.currentTimeMillis() - timestampMs).coerceAtLeast(0L)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed)
    return when {
        minutes < 1L -> "Just now"
        minutes == 1L -> "1 minute ago"
        minutes < 60L -> "$minutes minutes ago"
        else -> {
            val hours = TimeUnit.MILLISECONDS.toHours(elapsed)
            if (hours == 1L) "1 hour ago" else "$hours hours ago"
        }
    }
}

@Suppress("DEPRECATION")
private fun vibrateStrongWarning(context: Context) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        } ?: return
        // Pulsed warning ~2s total (6×250ms buzz + 5×100ms gaps).
        val pattern = longArrayOf(
            0,
            250, 100, 250, 100, 250, 100, 250, 100, 250, 100, 250
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            vibrator.vibrate(pattern, -1)
        }
    } catch (_: Exception) {
        // Best-effort haptic; never block dial confirmation.
    }
}
