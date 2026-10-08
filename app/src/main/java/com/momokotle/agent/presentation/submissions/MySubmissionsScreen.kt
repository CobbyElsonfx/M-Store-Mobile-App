package com.momokotle.agent.presentation.submissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.data.form.DocumentSlotRegistry
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.theme.AppDimens
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
                items(submissions, key = { it.submissionId }) { submission ->
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
    val submission by viewModel.submission.collectAsStateWithLifecycle()

    LaunchedEffect(submissionId) {
        viewModel.loadSubmission(submissionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submission Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        val current = submission
        if (current == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Submission not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SubmissionStatusBadge(status = current.status)
                DetailRow("Submission ID", current.submissionId)
                DetailRow("Form type", current.formType.displayName)
                DetailRow("Status", current.status.firestoreValue)
                DetailRow("Submitted", formatDate(current.createdAt))
                DetailRow("Last updated", formatDate(current.updatedAt))
                current.adminNote?.let { note ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                "Admin note",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                note,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                Text(
                    "Submitted information",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                current.formData.forEach { (key, value) ->
                    if (value.isNotBlank()) {
                        DetailRow(key, value)
                    }
                }
                if (current.uploadedDocuments.isNotEmpty()) {
                    val uriHandler = LocalUriHandler.current
                    Text(
                        "Uploaded documents",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    current.uploadedDocuments.forEach { (slotKey, url) ->
                        val label = DocumentSlotRegistry.mappingFor(slotKey).firestoreUrlKey
                            .let { DocumentSlotRegistry.documentDisplayName(it) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DetailRow(label, slotKey)
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

@Composable
private fun SubmissionCard(submission: FormSubmission, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
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
                SubmissionStatusBadge(status = submission.status)
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
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SubmissionStatusBadge(status: FormSubmissionStatus) {
    val (background, content) = when (status) {
        FormSubmissionStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer to
            MaterialTheme.colorScheme.onTertiaryContainer
        FormSubmissionStatus.APPROVED -> MaterialTheme.colorScheme.primaryContainer to
            MaterialTheme.colorScheme.onPrimaryContainer
        FormSubmissionStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer to
            MaterialTheme.colorScheme.onErrorContainer
        FormSubmissionStatus.RESUBMISSION_REQUIRED -> MaterialTheme.colorScheme.secondaryContainer to
            MaterialTheme.colorScheme.onSecondaryContainer
    }
    val label = when (status) {
        FormSubmissionStatus.RESUBMISSION_REQUIRED -> "Resubmission Required"
        else -> status.firestoreValue
    }
    Surface(shape = MaterialTheme.shapes.small, color = background) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = FontWeight.Medium
        )
    }
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

private val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

private fun formatDate(timestamp: Long): String =
    if (timestamp <= 0L) "—" else dateFormatter.format(Date(timestamp))
