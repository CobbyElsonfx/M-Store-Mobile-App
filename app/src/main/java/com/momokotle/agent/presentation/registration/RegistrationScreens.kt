package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.data.form.FormFieldKind
import com.momokotle.agent.data.form.FormSection
import com.momokotle.agent.data.form.FormTextField
import com.momokotle.agent.data.form.FormUploadKind
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.DocumentUploadField
import com.momokotle.agent.ui.components.FormDateField
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.utils.FormValidators
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun AgentRegistrationScreen(onFormClick: (AgentFormType) -> Unit) {
    AppScaffold(title = "Agent Registration") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ListItemSpacing)
        ) {
            AppSectionHeader(
                title = "Registration forms",
                subtitle = "Select a form to complete. Internet is required to submit."
            )
            AgentFormType.entries.forEach { type ->
                AppCard(onClick = { onFormClick(type) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            AgentFormDefinitions.get(type).title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

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

    AppScaffold(
        title = uiState.definition.title,
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            uiState.message?.let { AppInfoBanner(it) }
            uiState.errorMessage?.let { AppErrorBanner(it) }

            uiState.definition.standaloneFields.forEach { field ->
                FormFieldInput(
                    field = field,
                    value = uiState.fieldValues[field.key].orEmpty(),
                    errorText = uiState.fieldErrors[field.key],
                    isValid = uiState.fieldValid[field.key] == true,
                    showValidation = shouldShowFieldValidation(uiState, field.key),
                    onValueChange = { viewModel.onFieldChange(field.key, it) },
                    onDateSelected = { viewModel.onDateSelected(field.key, it) },
                    onBlur = { viewModel.onFieldBlur(field.key) },
                    onTouched = { viewModel.onFieldTouched(field.key) }
                )
            }
            uiState.definition.standaloneUploads.forEach { slot ->
                UploadSlot(
                    slot = slot,
                    fileName = uiState.uploadFileNames[slot.key],
                    localPath = uiState.previewPaths[slot.key],
                    uploadProgress = uiState.uploadProgress[slot.key],
                    isUploading = slot.key in uiState.uploadingSlots,
                    errorText = uiState.uploadErrors[slot.key],
                    isValid = uiState.uploadValid[slot.key] == true,
                    showValidation = shouldShowUploadValidation(uiState, slot.key),
                    onPick = {
                        pendingSlot = slot
                        picker.launch(mimeTypeFor(slot.kind))
                    },
                    onRemove = { viewModel.removeUpload(slot.key) },
                    onTouched = { viewModel.onUploadTouched(slot.key) }
                )
            }

            uiState.definition.sections.forEach { section ->
                FormSectionContent(
                    section = section,
                    fieldValues = uiState.fieldValues,
                    uploadFileNames = uiState.uploadFileNames,
                    uploads = uiState.uploads,
                    fieldErrors = uiState.fieldErrors,
                    fieldValid = uiState.fieldValid,
                    uploadErrors = uiState.uploadErrors,
                    uploadValid = uiState.uploadValid,
                    uploadProgress = uiState.uploadProgress,
                    uploadingSlots = uiState.uploadingSlots,
                    previewPaths = uiState.previewPaths,
                    touchedFields = uiState.touchedFields,
                    touchedUploads = uiState.touchedUploads,
                    validationAttempted = uiState.validationAttempted,
                    onFieldChange = viewModel::onFieldChange,
                    onDateSelected = viewModel::onDateSelected,
                    onFieldBlur = viewModel::onFieldBlur,
                    onFieldTouched = viewModel::onFieldTouched,
                    onPickUpload = { slot ->
                        pendingSlot = slot
                        picker.launch(mimeTypeFor(slot.kind))
                    },
                    onRemoveUpload = viewModel::removeUpload,
                    onUploadTouched = viewModel::onUploadTouched
                )
            }

            AppButton(
                text = if (uiState.isSubmitting) "Submitting…" else "Submit Request",
                onClick = viewModel::submit,
                enabled = !uiState.isSubmitting && uiState.isFormValid,
                style = AppButtonStyle.Accent
            )
        }
    }
}

@Composable
private fun FormFieldInput(
    field: FormTextField,
    value: String,
    errorText: String?,
    isValid: Boolean,
    showValidation: Boolean,
    onValueChange: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    onBlur: () -> Unit,
    onTouched: () -> Unit
) {
    when (field.kind) {
        FormFieldKind.DATE_OF_BIRTH -> FormDateField(
            label = field.label,
            value = value,
            placeholder = FormValidators.placeholderFor(field),
            errorText = errorText,
            isValid = isValid,
            showValidation = showValidation,
            onDateSelected = { date ->
                onTouched()
                onDateSelected(date)
            }
        )
        else -> {
            val keyboard = when (field.kind) {
                FormFieldKind.PHONE -> KeyboardOptions(keyboardType = KeyboardType.Number)
                FormFieldKind.GHANA_CARD -> KeyboardOptions(keyboardType = KeyboardType.Text)
                else -> KeyboardOptions.Default
            }
            AppTextField(
                value = value,
                onValueChange = {
                    onTouched()
                    onValueChange(it)
                },
                label = field.label,
                placeholder = FormValidators.placeholderFor(field).ifBlank { null },
                keyboardOptions = keyboard,
                errorText = errorText,
                isValid = isValid,
                showValidation = showValidation,
                maxLength = FormValidators.maxLengthFor(field),
                modifier = Modifier.onFocusChanged { state ->
                    if (!state.isFocused) onBlur()
                }
            )
        }
    }
}

@Composable
private fun FormSectionContent(
    section: FormSection,
    fieldValues: Map<String, String>,
    uploadFileNames: Map<String, String>,
    uploads: Map<String, String>,
    fieldErrors: Map<String, String>,
    fieldValid: Map<String, Boolean>,
    uploadErrors: Map<String, String>,
    uploadValid: Map<String, Boolean>,
    uploadProgress: Map<String, Int>,
    uploadingSlots: Set<String>,
    previewPaths: Map<String, String>,
    touchedFields: Set<String>,
    touchedUploads: Set<String>,
    validationAttempted: Boolean,
    onFieldChange: (String, String) -> Unit,
    onDateSelected: (String, String) -> Unit,
    onFieldBlur: (String) -> Unit,
    onFieldTouched: (String) -> Unit,
    onPickUpload: (FormUploadSlot) -> Unit,
    onRemoveUpload: (String) -> Unit,
    onUploadTouched: (String) -> Unit
) {
    AppCard {
        AppSectionHeader(title = section.title, subtitle = section.instruction)
        section.fields.forEach { field ->
            FormFieldInput(
                field = field,
                value = fieldValues[field.key].orEmpty(),
                errorText = fieldErrors[field.key],
                isValid = fieldValid[field.key] == true,
                showValidation = validationAttempted ||
                    field.key in touchedFields ||
                    fieldValues[field.key].orEmpty().isNotBlank(),
                onValueChange = { onFieldChange(field.key, it) },
                onDateSelected = { onDateSelected(field.key, it) },
                onBlur = { onFieldBlur(field.key) },
                onTouched = { onFieldTouched(field.key) }
            )
        }
        section.uploads.forEach { slot ->
            UploadSlot(
                slot = slot,
                fileName = uploadFileNames[slot.key],
                localPath = previewPaths[slot.key],
                uploadProgress = uploadProgress[slot.key],
                isUploading = slot.key in uploadingSlots,
                errorText = uploadErrors[slot.key],
                isValid = uploadValid[slot.key] == true,
                showValidation = validationAttempted ||
                    slot.key in touchedUploads ||
                    uploads.containsKey(slot.key) ||
                    previewPaths.containsKey(slot.key),
                onPick = { onPickUpload(slot) },
                onRemove = { onRemoveUpload(slot.key) },
                onTouched = { onUploadTouched(slot.key) }
            )
        }
    }
}

@Composable
private fun UploadSlot(
    slot: FormUploadSlot,
    fileName: String?,
    localPath: String?,
    uploadProgress: Int?,
    isUploading: Boolean,
    errorText: String?,
    isValid: Boolean,
    showValidation: Boolean,
    onPick: () -> Unit,
    onRemove: () -> Unit,
    onTouched: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        DocumentUploadField(
            label = slot.label,
            instruction = slot.instruction,
            selectedFileName = when {
                isUploading -> fileName?.let { "$it — uploading ${uploadProgress ?: 0}%" }
                fileName != null -> fileName
                else -> null
            },
            localPath = localPath,
            errorText = errorText,
            isValid = isValid && !isUploading,
            showValidation = showValidation && !isUploading,
            showImagePreview = localPath != null,
            onPick = {
                onTouched()
                onPick()
            },
            onRemove = {
                onTouched()
                onRemove()
            }
        )
        if (isUploading) {
            LinearProgressIndicator(
                progress = { (uploadProgress ?: 0) / 100f },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun shouldShowFieldValidation(
    uiState: AgentFormUiState,
    key: String
): Boolean = uiState.validationAttempted ||
    key in uiState.touchedFields ||
    uiState.fieldValues[key].orEmpty().isNotBlank()

private fun shouldShowUploadValidation(
    uiState: AgentFormUiState,
    key: String
): Boolean = uiState.validationAttempted ||
    key in uiState.touchedUploads ||
    uiState.uploads.containsKey(key)

private fun mimeTypeFor(kind: FormUploadKind): String = when (kind) {
    FormUploadKind.SIGNATURE -> "image/*"
    FormUploadKind.DOCUMENT -> "*/*"
}
