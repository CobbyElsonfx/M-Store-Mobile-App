package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.data.form.FormTextField
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.utils.FileDocumentHelper
import com.momokotle.agent.utils.FormValidators
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AgentFormUiState(
    val definition: com.momokotle.agent.data.form.AgentFormDefinition,
    val submissionId: String? = null,
    val fieldValues: Map<String, String> = emptyMap(),
    val uploads: Map<String, String> = emptyMap(),
    val uploadFileNames: Map<String, String> = emptyMap(),
    val uploadProgress: Map<String, Int> = emptyMap(),
    val uploadingSlots: Set<String> = emptySet(),
    val previewPaths: Map<String, String> = emptyMap(),
    val fieldErrors: Map<String, String> = emptyMap(),
    val fieldValid: Map<String, Boolean> = emptyMap(),
    val uploadErrors: Map<String, String> = emptyMap(),
    val uploadValid: Map<String, Boolean> = emptyMap(),
    val touchedFields: Set<String> = emptySet(),
    val touchedUploads: Set<String> = emptySet(),
    val validationAttempted: Boolean = false,
    val isFormValid: Boolean = false,
    val isSubmitting: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AgentFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val formSubmissionRepository: FormSubmissionRepository,
    private val getAuthenticatedUserUseCase: GetAuthenticatedUserUseCase,
    private val networkMonitor: NetworkMonitor,
    private val fileDocumentHelper: FileDocumentHelper
) : ViewModel() {

    private val formType: AgentFormType = AgentFormType.valueOf(
        savedStateHandle.get<String>("formType") ?: AgentFormType.AGENT_CARD_REQUEST.name
    )

    private val _uiState = MutableStateFlow(
        AgentFormUiState(definition = AgentFormDefinitions.get(formType))
    )
    val uiState: StateFlow<AgentFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { formSubmissionRepository.generateSubmissionId() }
                .onSuccess { id ->
                    _uiState.update { it.copy(submissionId = id) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(errorMessage = e.message ?: "Could not prepare submission ID")
                    }
                }
        }
    }

    fun onFieldChange(key: String, raw: String) {
        val field = findField(key) ?: return
        val filtered = FormValidators.filterInput(field, raw)
        _uiState.update { state ->
            val nextValues = state.fieldValues + (key to filtered)
            val touched = state.touchedFields + key
            recompute(state.copy(fieldValues = nextValues, touchedFields = touched))
        }
    }

    fun onFieldBlur(key: String) {
        val field = findField(key) ?: return
        _uiState.update { state ->
            val raw = state.fieldValues[key].orEmpty()
            val normalized = FormValidators.normalizeField(field, raw)
            recompute(
                state.copy(
                    fieldValues = state.fieldValues + (key to normalized),
                    touchedFields = state.touchedFields + key
                )
            )
        }
    }

    fun onDateSelected(key: String, formattedDate: String) {
        _uiState.update { state ->
            val nextValues = state.fieldValues + (key to formattedDate)
            val touched = state.touchedFields + key
            recompute(state.copy(fieldValues = nextValues, touchedFields = touched))
        }
    }

    fun onFieldTouched(key: String) {
        _uiState.update { state ->
            recompute(state.copy(touchedFields = state.touchedFields + key))
        }
    }

    fun onUploadTouched(key: String) {
        _uiState.update { state ->
            recompute(state.copy(touchedUploads = state.touchedUploads + key))
        }
    }

    fun onFileSelected(slot: FormUploadSlot, uri: Uri) {
        viewModelScope.launch {
            val state = _uiState.value
            val submissionId = state.submissionId
            if (submissionId.isNullOrBlank()) {
                _uiState.update { it.copy(errorMessage = "Preparing submission. Please try again in a moment.") }
                return@launch
            }
            if (!networkMonitor.isOnline()) {
                _uiState.update { it.copy(errorMessage = InternetMessages.FORM_REQUIRES_INTERNET) }
                return@launch
            }

            fileDocumentHelper.copyToInternalStorage(uri, slot.key, slot.kind)
                .onSuccess { localPath ->
                    _uiState.update { current ->
                        recompute(
                            current.copy(
                                previewPaths = current.previewPaths + (slot.key to localPath),
                                uploadFileNames = current.uploadFileNames + (
                                    slot.key to fileDocumentHelper.fileNameFromPath(localPath)
                                    ),
                                uploadingSlots = current.uploadingSlots + slot.key,
                                uploadProgress = current.uploadProgress + (slot.key to 0),
                                touchedUploads = current.touchedUploads + slot.key,
                                uploadErrors = current.uploadErrors - slot.key
                            )
                        )
                    }
                    uploadToFirebase(slot, localPath, submissionId)
                }
                .onFailure { e ->
                    _uiState.update { current ->
                        recompute(
                            current.copy(
                                uploadErrors = current.uploadErrors + (slot.key to (e.message ?: "Could not read file")),
                                uploadValid = current.uploadValid + (slot.key to false),
                                touchedUploads = current.touchedUploads + slot.key,
                                errorMessage = e.message ?: "Could not read file"
                            )
                        )
                    }
                }
        }
    }

    private suspend fun uploadToFirebase(slot: FormUploadSlot, localPath: String, submissionId: String) {
        val profile = try {
            getAuthenticatedUserUseCase()
        } catch (_: Exception) {
            _uiState.update { it.copy(errorMessage = "Please sign in to upload documents") }
            return
        }

        formSubmissionRepository.uploadDocumentSlot(
            userId = profile.userId,
            submissionId = submissionId,
            slotId = slot.key,
            localPath = localPath,
            onProgress = { percent ->
                _uiState.update { it.copy(uploadProgress = it.uploadProgress + (slot.key to percent)) }
            }
        ).onSuccess { downloadUrl ->
            _uiState.update { current ->
                recompute(
                    current.copy(
                        uploads = current.uploads + (slot.key to downloadUrl),
                        uploadingSlots = current.uploadingSlots - slot.key,
                        uploadProgress = current.uploadProgress + (slot.key to 100),
                        uploadErrors = current.uploadErrors - slot.key
                    )
                )
            }
        }.onFailure { e ->
            _uiState.update { current ->
                recompute(
                    current.copy(
                        uploads = current.uploads - slot.key,
                        uploadingSlots = current.uploadingSlots - slot.key,
                        uploadProgress = current.uploadProgress - slot.key,
                        uploadErrors = current.uploadErrors + (slot.key to (e.message ?: "Upload failed")),
                        errorMessage = e.message ?: "Upload failed"
                    )
                )
            }
        }
    }

    fun removeUpload(slotKey: String) {
        _uiState.update { state ->
            recompute(
                state.copy(
                    uploads = state.uploads - slotKey,
                    uploadFileNames = state.uploadFileNames - slotKey,
                    previewPaths = state.previewPaths - slotKey,
                    uploadProgress = state.uploadProgress - slotKey,
                    uploadingSlots = state.uploadingSlots - slotKey,
                    touchedUploads = state.touchedUploads + slotKey
                )
            )
        }
    }

    fun submit() {
        _uiState.update { recompute(it.copy(validationAttempted = true)) }
        val state = _uiState.value
        if (!state.isFormValid) {
            val firstFieldError = state.fieldErrors.values.firstOrNull()
            val firstUploadError = state.uploadErrors.values.firstOrNull()
            _uiState.update {
                it.copy(errorMessage = firstFieldError ?: firstUploadError ?: "Please fix the highlighted fields")
            }
            return
        }
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.FORM_REQUIRES_INTERNET) }
            return
        }

        if (state.uploadingSlots.isNotEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please wait for all document uploads to finish") }
            return
        }
        if (state.submissionId.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Submission is not ready. Please try again.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val profile = try {
                getAuthenticatedUserUseCase()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = "Please sign in to submit this form")
                }
                return@launch
            }
            val normalizedData = buildNormalizedFieldValues(state)
            val submission = FormSubmission(
                submissionId = state.submissionId,
                userId = profile.userId,
                formType = formType,
                formData = normalizedData,
                uploadedDocuments = state.uploads,
                status = FormSubmissionStatus.PENDING
            )
            formSubmissionRepository.createAndSubmitForm(submission)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            message = "Request ${result.status.firestoreValue} successfully"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = e.message ?: "Submission failed"
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }

    private fun findField(key: String): FormTextField? =
        _uiState.value.definition.allFields().find { it.key == key }

    private fun buildNormalizedFieldValues(state: AgentFormUiState): Map<String, String> =
        state.definition.allFields().mapNotNull { field ->
            val raw = state.fieldValues[field.key].orEmpty()
            if (!field.required && raw.isBlank()) {
                field.key to ""
            } else {
                field.key to FormValidators.normalizeField(field, raw)
            }
        }.toMap()

    private fun recompute(state: AgentFormUiState): AgentFormUiState {
        val def = state.definition
        val fieldErrors = mutableMapOf<String, String>()
        val fieldValid = mutableMapOf<String, Boolean>()
        def.allFields().forEach { field ->
            val value = state.fieldValues[field.key].orEmpty()
            val error = FormValidators.validateField(field, value)
            val show = shouldShowFieldValidation(state, field.key, value)
            if (show && error != null) fieldErrors[field.key] = error
            fieldValid[field.key] = FormValidators.isFieldValid(field, value)
        }

        val uploadErrors = mutableMapOf<String, String>()
        val uploadValid = mutableMapOf<String, Boolean>()
        def.allUploads().forEach { slot ->
            val value = state.uploads[slot.key]
            val error = FormValidators.validateUploadedDocument(slot, value, slot.key in state.uploadingSlots)
            val show = shouldShowUploadValidation(state, slot.key, value)
            if (show && error != null) uploadErrors[slot.key] = error
            uploadValid[slot.key] = FormValidators.isUploadedDocumentValid(slot, value, slot.key in state.uploadingSlots)
        }

        val uploadsComplete = state.uploadingSlots.isEmpty()
        val isFormValid = uploadsComplete &&
            def.allFields().all { FormValidators.isFieldValid(it, state.fieldValues[it.key].orEmpty()) } &&
            def.allUploads().all {
                FormValidators.isUploadedDocumentValid(it, state.uploads[it.key], it.key in state.uploadingSlots)
            }

        return state.copy(
            fieldErrors = fieldErrors,
            fieldValid = fieldValid,
            uploadErrors = uploadErrors,
            uploadValid = uploadValid,
            isFormValid = isFormValid
        )
    }

    private fun shouldShowFieldValidation(state: AgentFormUiState, key: String, value: String): Boolean =
        state.validationAttempted || key in state.touchedFields || value.isNotBlank()

    private fun shouldShowUploadValidation(state: AgentFormUiState, key: String, path: String?): Boolean =
        state.validationAttempted || key in state.touchedUploads || !path.isNullOrBlank()
}
