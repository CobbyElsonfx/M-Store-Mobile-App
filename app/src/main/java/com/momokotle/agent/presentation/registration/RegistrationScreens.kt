package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.data.form.FormSection
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.DocumentUploadField
import com.momokotle.agent.ui.components.ErrorBanner
import com.momokotle.agent.ui.components.InfoBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentRegistrationScreen(onFormClick: (AgentFormType) -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Agent Registration") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Select a form to complete. Internet is required to submit.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            AgentFormType.entries.forEach { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFormClick(type) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            AgentFormDefinitions.get(type).title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentFormScreen(
    onBack: () -> Unit,
    viewModel: AgentFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var pendingSlot by remember { mutableStateOf<FormUploadSlot?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val slot = pendingSlot ?: return@rememberLauncherForActivityResult
        if (uri != null) viewModel.onFileSelected(slot, uri)
        pendingSlot = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.definition.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.message?.let { InfoBanner(it) }
            uiState.errorMessage?.let { ErrorBanner(it) }

            uiState.definition.standaloneFields.forEach { field ->
                AgentTextField(
                    value = uiState.fieldValues[field.key].orEmpty(),
                    onValueChange = { viewModel.onFieldChange(field.key, it) },
                    label = field.label
                )
            }
            uiState.definition.standaloneUploads.forEach { slot ->
                UploadSlot(
                    slot = slot,
                    fileName = uiState.uploadFileNames[slot.key],
                    onPick = { pendingSlot = slot; picker.launch("*/*") },
                    onRemove = { viewModel.removeUpload(slot.key) }
                )
            }

            uiState.definition.sections.forEach { section ->
                FormSectionContent(
                    section = section,
                    fieldValues = uiState.fieldValues,
                    uploadFileNames = uiState.uploadFileNames,
                    onFieldChange = viewModel::onFieldChange,
                    onPickUpload = { slot -> pendingSlot = slot; picker.launch("*/*") },
                    onRemoveUpload = viewModel::removeUpload
                )
            }

            AgentActionButton(
                text = if (uiState.isSubmitting) "Submitting…" else "Submit Request",
                onClick = viewModel::submit,
                enabled = !uiState.isSubmitting
            )
        }
    }
}

@Composable
private fun FormSectionContent(
    section: FormSection,
    fieldValues: Map<String, String>,
    uploadFileNames: Map<String, String>,
    onFieldChange: (String, String) -> Unit,
    onPickUpload: (FormUploadSlot) -> Unit,
    onRemoveUpload: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(section.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            section.instruction?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            section.fields.forEach { field ->
                AgentTextField(
                    value = fieldValues[field.key].orEmpty(),
                    onValueChange = { onFieldChange(field.key, it) },
                    label = field.label
                )
            }
            section.uploads.forEach { slot ->
                UploadSlot(
                    slot = slot,
                    fileName = uploadFileNames[slot.key],
                    onPick = { onPickUpload(slot) },
                    onRemove = { onRemoveUpload(slot.key) }
                )
            }
        }
    }
}

@Composable
private fun UploadSlot(
    slot: FormUploadSlot,
    fileName: String?,
    onPick: () -> Unit,
    onRemove: () -> Unit
) {
    DocumentUploadField(
        label = slot.label,
        instruction = slot.instruction,
        selectedFileName = fileName,
        onPick = onPick,
        onRemove = onRemove
    )
}
