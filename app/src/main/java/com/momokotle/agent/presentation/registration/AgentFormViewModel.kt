package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.form.AgentFormDefinition
import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.UserIdProvider
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.utils.FileDocumentHelper
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AgentFormUiState(
    val definition: AgentFormDefinition,
    val fieldValues: Map<String, String> = emptyMap(),
    val uploads: Map<String, String> = emptyMap(),
    val uploadFileNames: Map<String, String> = emptyMap(),
    val isSubmitting: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AgentFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val formSubmissionRepository: FormSubmissionRepository,
    private val userIdProvider: UserIdProvider,
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

    fun onFieldChange(key: String, value: String) {
        _uiState.update { it.copy(fieldValues = it.fieldValues + (key to value)) }
    }

    fun onFileSelected(slot: FormUploadSlot, uri: Uri) {
        viewModelScope.launch {
            fileDocumentHelper.copyToInternalStorage(uri, slot.key)
                .onSuccess { path -> setUpload(slot, path) }
                .onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message ?: "Could not read file") }
                }
        }
    }

    private fun setUpload(slot: FormUploadSlot, localPath: String) {
        _uiState.update {
            it.copy(
                uploads = it.uploads + (slot.key to localPath),
                uploadFileNames = it.uploadFileNames + (slot.key to fileDocumentHelper.fileNameFromPath(localPath))
            )
        }
    }

    fun removeUpload(slotKey: String) {
        _uiState.update {
            it.copy(
                uploads = it.uploads - slotKey,
                uploadFileNames = it.uploadFileNames - slotKey
            )
        }
    }

    fun submit() {
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.FORM_REQUIRES_INTERNET) }
            return
        }
        val state = _uiState.value
        val def = state.definition
        val allFields = def.standaloneFields + def.sections.flatMap { it.fields }
        val missingField = allFields.firstOrNull { f ->
            f.required && state.fieldValues[f.key].orEmpty().isBlank()
        }
        if (missingField != null) {
            _uiState.update { it.copy(errorMessage = "Enter ${missingField.label}") }
            return
        }
        val allUploads = def.standaloneUploads + def.sections.flatMap { it.uploads }
        val missingUpload = allUploads.firstOrNull { u ->
            u.required && !state.uploads.containsKey(u.key)
        }
        if (missingUpload != null) {
            _uiState.update { it.copy(errorMessage = "Upload ${missingUpload.label}") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val userId = userIdProvider.getUserId()
            val submission = FormSubmission(
                submissionId = UUID.randomUUID().toString(),
                userId = userId,
                formType = formType,
                formData = state.fieldValues,
                uploadedDocuments = state.uploads,
                status = FormSubmissionStatus.PENDING
            )
            formSubmissionRepository.createAndSubmitForm(submission)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            message = "Request ${result.status.name.lowercase()} successfully"
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
}
