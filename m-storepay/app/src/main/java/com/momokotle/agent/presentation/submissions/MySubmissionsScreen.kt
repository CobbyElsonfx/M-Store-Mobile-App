package com.momokotle.agent.presentation.submissions

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.data.form.DocumentSlotRegistry
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.presentation.subscription.PaystackCheckoutActivity
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppSuccessBanner
import com.momokotle.agent.ui.theme.AppDimens
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySubmissionsScreen(
    onBack: () -> Unit,
    onSubmissionClick: (String) -> Unit,
    viewModel: MySubmissionsViewModel = hiltViewModel()
) {
    val submissions by viewModel.submissions.collectAsStateWithLifecycle()

    AppScaffold(title = "My Submissions", onBack = onBack) { padding ->
        if (submissions.isEmpty()) {
            AppEmptyState(
                title = "No submissions yet",
                message = "Submitted registration forms appear here",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimens.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(
                    submissions,
                    key = { index, submission -> "${submission.submissionId}#$index" }
                ) { _, submission ->
                    SubmissionCard(
                        submission = submission,
                        onClick = { onSubmissionClick(submission.submissionId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionDetailScreen(
    submissionId: String,
    onBack: () -> Unit,
    viewModel: SubmissionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val submission = uiState.submission
    val context = LocalContext.current
    val checkoutLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> viewModel.onCheckoutCompleted()
            PaystackCheckoutActivity.RESULT_FAILED -> viewModel.onCheckoutFailed(
                result.data?.getStringExtra(PaystackCheckoutActivity.EXTRA_ERROR)
            )
            else -> viewModel.onCheckoutCancelled()
        }
    }

    LaunchedEffect(submissionId) {
        viewModel.loadSubmission(submissionId)
    }
    LaunchedEffect(uiState.copiedLabel) {
        if (uiState.copiedLabel != null) {
            delay(1_500)
            viewModel.consumeCopied()
        }
    }
    LaunchedEffect(uiState.accessCodeToLaunch) {
        val accessCode = uiState.accessCodeToLaunch ?: return@LaunchedEffect
        val publicKey = uiState.checkout?.publicKey.orEmpty()
        viewModel.consumeLaunch()
        if (publicKey.isBlank()) {
            viewModel.onCheckoutFailed("Paystack public key was not provided by the server.")
            return@LaunchedEffect
        }
        checkoutLauncher.launch(PaystackCheckoutActivity.intent(context, publicKey, accessCode))
    }

    AppScaffold(title = "Submission Details", onBack = onBack) { padding ->
        val current = submission
        if (current == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Submission not found", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val amount = current.paymentAmount.takeIf { it > 0 }
                ?.let { "GHS ${"%.2f".format(it)}" }
                ?: existingAmount(current.formData)
            val shownKeys = setOf("amount", "Amount", "total", "Total")
            val needsPayment = current.requiresPayment &&
                current.paymentStatus?.equals("Confirmed", ignoreCase = true) != true
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.errorMessage?.let { AppErrorBanner(it) }
                uiState.message?.let { AppInfoBanner(it) }

                AppCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubmissionStatusBadge(status = current.status)
                            SubmissionPaymentBadge(submission = current)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Reference",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                current.submissionId,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailRow("Service", current.formType.displayName)
                        if (current.applicantName.isNotBlank()) {
                            DetailRow("Applicant", current.applicantName)
                        }
                        if (current.phoneNumber.isNotBlank()) {
                            DetailRow("Recipient", current.phoneNumber)
                        }
                        if (amount != null) {
                            DetailRow("Amount", amount)
                        }
                        DetailRow("Payment", current.paymentLabel)
                        DetailRow("Date", formatDate(current.createdAt))
                        DetailRow("Last updated", formatDate(current.updatedAt))
                    }
                }

                if (needsPayment) {
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                "Choose how to pay for this service.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            AppButton(
                                text = if (amount != null) "Pay with Paystack ($amount)" else "Pay with Paystack",
                                onClick = viewModel::startPaystackPayment,
                                isLoading = uiState.isPayingPaystack,
                                enabled = !uiState.isBusy
                            )
                            AppButton(
                                text = if (amount != null) "Pay with MoMo / bank ($amount)" else "Pay with MoMo / bank",
                                onClick = viewModel::startManualPayment,
                                isLoading = uiState.isPayingManual,
                                enabled = !uiState.isBusy,
                                style = AppButtonStyle.Outlined
                            )
                        }
                    }
                }

                uiState.manualCheckout?.let { manual ->
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                "Manual MoMo / bank payment",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "${manual.currency} ${"%.2f".format(manual.amount)} · ${manual.planName.ifBlank { current.formType.displayName }}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            SubmissionCopyableDetail(
                                label = "Reference",
                                value = manual.reference,
                                onCopy = {
                                    viewModel.copyToClipboard(context, "Reference", manual.reference)
                                }
                            )
                            listOf(
                                "Account name" to manual.instructions.accountName,
                                "Account number" to manual.instructions.accountNumber,
                                "Bank" to manual.instructions.bankName,
                                "MoMo network" to manual.instructions.momoNetwork,
                                "MoMo number" to manual.instructions.momoNumber
                            ).filter { it.second.isNotBlank() }.forEach { (label, value) ->
                                SubmissionCopyableDetail(
                                    label = label,
                                    value = value,
                                    onCopy = { viewModel.copyToClipboard(context, label, value) }
                                )
                            }
                            if (manual.instructions.instructions.isNotBlank()) {
                                DetailRow("Instructions", manual.instructions.instructions)
                            }
                            AppInfoBanner(
                                "Send the exact amount and include your reference in the transfer note. An admin will mark this paid after the payment is received."
                            )
                            AppButton(
                                text = if (uiState.isCheckingStatus) "Checking…" else "Check payment status",
                                onClick = viewModel::checkManualPaymentStatus,
                                enabled = !uiState.isBusy,
                                style = AppButtonStyle.Outlined
                            )
                        }
                    }
                }

                uiState.copiedLabel?.let { AppSuccessBanner("Copied $it") }

                current.adminNote?.let { note ->
                    AppCard {
                        Text(
                            "Admin note",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            note,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                val extraFields = current.formData.mapNotNull { (key, raw) ->
                    val value = raw.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    if (key in shownKeys) null else key to value
                }
                if (extraFields.isNotEmpty()) {
                    AppCard {
                        Text(
                            "Submitted information",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            extraFields.forEach { (key, value) ->
                                DetailRow(key, value)
                            }
                        }
                    }
                }

                if (current.uploadedDocuments.isNotEmpty()) {
                    val uriHandler = LocalUriHandler.current
                    AppCard {
                        Text(
                            "Uploaded documents",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            current.uploadedDocuments.forEach { (slotKey, url) ->
                                val label = DocumentSlotRegistry.mappingFor(slotKey).firestoreUrlKey
                                    .let { DocumentSlotRegistry.documentDisplayName(it) }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        DetailRow(label, slotKey)
                                    }
                                    if (url.startsWith("http", ignoreCase = true)) {
                                        TextButton(onClick = { uriHandler.openUri(url) }) {
                                            Text("View")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmissionCard(submission: FormSubmission, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = submission.applicantName.ifBlank { submission.formType.displayName },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = submission.submissionId,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SubmissionStatusBadge(status = submission.status)
                    SubmissionPaymentBadge(submission = submission)
                }
            }
            Text(
                text = submission.formType.displayName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = formatDate(submission.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            submission.adminNote?.let { note ->
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun SubmissionStatusBadge(status: FormSubmissionStatus) {
    val tone = when (status) {
        FormSubmissionStatus.AWAITING_PAYMENT -> AppStatusTone.Warning
        FormSubmissionStatus.PENDING -> AppStatusTone.Warning
        FormSubmissionStatus.APPROVED -> AppStatusTone.Success
        FormSubmissionStatus.REJECTED -> AppStatusTone.Error
        FormSubmissionStatus.RESUBMISSION_REQUIRED -> AppStatusTone.Warning
    }
    val label = when (status) {
        FormSubmissionStatus.AWAITING_PAYMENT -> "Awaiting Payment"
        FormSubmissionStatus.RESUBMISSION_REQUIRED -> "Resubmission Required"
        else -> status.firestoreValue
    }
    AppStatusBadge(text = label, tone = tone)
}

@Composable
fun SubmissionPaymentBadge(submission: FormSubmission) {
    val label = submission.paymentLabel
    val tone = when (label) {
        "Paid" -> AppStatusTone.Success
        "Unpaid" -> AppStatusTone.Warning
        else -> AppStatusTone.Info
    }
    AppStatusBadge(text = label, tone = tone)
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SubmissionCopyableDetail(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
        TextButton(onClick = onCopy) {
            Text("Copy")
        }
    }
}

private fun existingAmount(formData: Map<String, String>): String? {
    val key = formData.keys.firstOrNull { it.equals("amount", ignoreCase = true) || it.equals("total", ignoreCase = true) }
        ?: return null
    return formData[key]?.takeIf { it.isNotBlank() }
}

private fun formatDate(timestamp: Long): String {
    if (timestamp <= 0L) return "—"
    return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
}
