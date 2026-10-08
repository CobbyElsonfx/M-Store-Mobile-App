package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.momokotle.agent.data.form.FormFieldKind
import com.momokotle.agent.data.form.FormSection
import com.momokotle.agent.data.form.FormTextField
import com.momokotle.agent.data.form.FormUploadKind
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.StoreProductForms
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppFormCard
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppSuccessBanner
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.DocumentUploadField
import com.momokotle.agent.ui.components.FormDateField
import com.momokotle.agent.ui.components.FormSelectField
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.utils.FormValidators
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun AgentRegistrationScreen(onFormClick: (AgentFormType) -> Unit) {
    AppScaffold(title = "Services") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.ListItemSpacing)
        ) {
            AppSectionHeader(
                title = "Choose a service",
                subtitle = "SIM and agent-network applications are on Store. Internet is required to submit."
            )
            AgentFormType.entries
                .filterNot { it in StoreProductForms.linkedServiceForms }
                .forEach { type ->
                AppCard(onClick = { onFormClick(type) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Surface(
                            modifier = Modifier.size(44.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    imageVector = serviceIcon(type),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                type.serviceTitle,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                type.serviceDescription,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Open ${type.serviceTitle}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun serviceIcon(type: AgentFormType): ImageVector = when (type) {
    AgentFormType.AGENT_CARD_REQUEST -> Icons.Filled.CreditCard
    AgentFormType.EVD_SIM_CONVERSION -> Icons.Filled.SimCard
    AgentFormType.DYNAMIC_SIM_BULK -> Icons.AutoMirrored.Filled.Assignment
    AgentFormType.TELECEL_AGENT -> Icons.Filled.PhoneAndroid
    AgentFormType.AIRTELTIGO_AGENT -> Icons.Filled.PhoneAndroid
    AgentFormType.BUSINESS_CERTIFICATE -> Icons.Filled.Business
    AgentFormType.BIRTH_CERTIFICATE -> Icons.Filled.ChildCare
    AgentFormType.PASSPORT -> Icons.Filled.FlightTakeoff
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
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.currentStepIndex) {
        scrollState.animateScrollTo(0)
    }

    LaunchedEffect(uiState.message) {
        if (!uiState.message.isNullOrBlank()) {
            pendingSlot = null
            scrollState.animateScrollTo(0)
        }
    }

    AppScaffold(
        title = uiState.definition.title,
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            uiState.message?.let { AppSuccessBanner(it) }
            uiState.errorMessage?.let { AppErrorBanner(it) }

            uiState.storeProduct?.let { product ->
                AppCard {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Quantity: 1 · Amount: GHS ${"%.2f".format(product.price)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (product.isAvailable) {
                        Text(
                            text = "Complete this form, then pay for this one item.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        AppErrorBanner(message = "This product is currently unavailable.")
                    }
                }
            }

            val definition = uiState.definition
            val stepped = definition.isStepped()
            val currentStep = definition.steps.getOrNull(uiState.currentStepIndex)
            val isReviewStep = currentStep?.isReview == true

            if (stepped && currentStep != null) {
                Text(
                    currentStep.subtitle ?: "Step ${uiState.currentStepIndex + 1} of ${definition.steps.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    currentStep.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (isReviewStep) {
                PassportReviewContent(
                    uiState = uiState,
                    onEditStep = viewModel::goToStep
                )
            } else {
                if (!stepped || definition.includeStandaloneForStep(uiState.currentStepIndex)) {
                    definition.standaloneFields.filter { it.isVisible(uiState.fieldValues) }.forEach { field ->
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
                    if (!stepped || definition.includeStandaloneForStep(uiState.currentStepIndex)) {
                        definition.standaloneUploads.forEach { slot ->
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
                    }
                }

                val sectionsToShow = if (stepped) {
                    definition.sectionsForStep(uiState.currentStepIndex)
                } else {
                    definition.sections
                }
                sectionsToShow.forEach { section ->
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
                        validationAttempted = uiState.validationAttempted || uiState.stepValidationAttempted,
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
            }

            if (stepped) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.currentStepIndex > 0) {
                        AppButton(
                            text = "Back",
                            onClick = viewModel::goToPreviousStep,
                            modifier = Modifier.weight(1f),
                            style = AppButtonStyle.Outlined
                        )
                    }
                    if (isReviewStep) {
                        AppButton(
                            text = submitButtonLabel(uiState),
                            onClick = viewModel::submit,
                            enabled = !uiState.isSubmitting && uiState.storeProduct?.isAvailable != false,
                            modifier = Modifier.weight(1f),
                            style = AppButtonStyle.Accent
                        )
                    } else {
                        val isLastContentStep =
                            uiState.currentStepIndex == definition.steps.lastIndex - 1
                        AppButton(
                            text = if (isLastContentStep) "Review Application" else "Next",
                            onClick = viewModel::goToNextStep,
                            modifier = Modifier.weight(1f),
                            style = AppButtonStyle.Accent
                        )
                    }
                }
            } else {
                AppButton(
                    text = submitButtonLabel(uiState),
                    onClick = viewModel::submit,
                    enabled = !uiState.isSubmitting && uiState.isFormValid && uiState.storeProduct?.isAvailable != false,
                    style = AppButtonStyle.Accent
                )
            }
        }
    }
}

private fun submitButtonLabel(uiState: AgentFormUiState): String {
    if (uiState.isSubmitting) return "Submitting…"
    val product = uiState.storeProduct ?: return if (uiState.definition.isStepped()) {
        "Submit Application"
    } else {
        "Submit Request"
    }
    return "Submit and pay GHS ${"%.2f".format(product.price)}"
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
        FormFieldKind.DATE_OF_BIRTH, FormFieldKind.DATE -> FormDateField(
            label = field.label,
            value = value,
            placeholder = FormValidators.placeholderFor(field),
            errorText = errorText,
            isValid = isValid,
            showValidation = showValidation,
            constrainToAdultDob = field.kind == FormFieldKind.DATE_OF_BIRTH,
            onDateSelected = { date ->
                onTouched()
                onDateSelected(date)
            }
        )
        FormFieldKind.SELECT -> FormSelectField(
            label = field.label,
            value = value,
            placeholder = FormValidators.placeholderFor(field),
            options = field.options,
            errorText = errorText,
            isValid = isValid,
            showValidation = showValidation,
            onOptionSelected = { option ->
                onTouched()
                onValueChange(option)
            }
        )
        else -> {
            val keyboard = when (field.kind) {
                FormFieldKind.PHONE, FormFieldKind.YEAR -> KeyboardOptions(keyboardType = KeyboardType.Number)
                FormFieldKind.EMAIL -> KeyboardOptions(keyboardType = KeyboardType.Email)
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
    val visibleFields = section.fields.filter { it.isVisible(fieldValues) }
    if (visibleFields.isEmpty() && section.uploads.isEmpty()) return

    AppFormCard {
        AppSectionHeader(title = section.title, subtitle = section.instruction)
        visibleFields.forEach { field ->
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
    uiState.stepValidationAttempted ||
    key in uiState.touchedUploads ||
    uiState.uploads.containsKey(key)

@Composable
private fun PassportReviewContent(
    uiState: AgentFormUiState,
    onEditStep: (Int) -> Unit
) {
    val definition = uiState.definition
    definition.steps.forEachIndexed { index, step ->
        if (step.isReview) return@forEachIndexed
        val sections = definition.sectionsForStep(index)
        AppCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    step.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Edit",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onEditStep(index) }
                )
            }
            sections.forEach { section ->
                val visibleFields = section.fields.filter { field ->
                    field.isVisible(uiState.fieldValues) &&
                        uiState.fieldValues[field.key].orEmpty().isNotBlank()
                }
                if (visibleFields.isEmpty() && section.uploads.none { uiState.uploads.containsKey(it.key) }) {
                    return@forEach
                }
                Text(
                    section.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                visibleFields.forEach { field ->
                    Text(
                        "${field.label}: ${uiState.fieldValues[field.key]}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                section.uploads.forEach { slot ->
                    val fileName = uiState.uploadFileNames[slot.key]
                    if (fileName != null) {
                        Text(
                            "${slot.label}: $fileName",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private fun mimeTypeFor(kind: FormUploadKind): String = when (kind) {
    FormUploadKind.SIGNATURE -> "image/*"
    FormUploadKind.DOCUMENT -> "*/*"
}
