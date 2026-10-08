package com.momokotle.agent.presentation.registration

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.data.form.FormTextField
import com.momokotle.agent.data.form.FormUploadSlot
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.utils.FileDocumentHelper
import com.momokotle.agent.utils.FormValidators
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import com.momokotle.agent.utils.PhoneNumberFormatter
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
    val storeProduct: com.momokotle.agent.models.StoreProduct? = null,
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
    val currentStepIndex: Int = 0,
    val stepValidationAttempted: Boolean = false,
    val isSubmitting: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

/** Empty copy of this state after a confirmed successful submit. Keeps only the form definition. */
fun AgentFormUiState.resetForm(successMessage: String): AgentFormUiState = AgentFormUiState(
    definition = definition,
    message = successMessage
)

@HiltViewModel
class AgentFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val formSubmissionRepository: FormSubmissionRepository,
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository,
    private val getAuthenticatedUserUseCase: GetAuthenticatedUserUseCase,
    private val networkMonitor: NetworkMonitor,
    private val fileDocumentHelper: FileDocumentHelper
) : ViewModel() {

    private val formType: AgentFormType = AgentFormType.valueOf(
        savedStateHandle.get<String>("formType") ?: AgentFormType.AGENT_CARD_REQUEST.name
    )
    private val productId: String? = savedStateHandle.get<String>("productId")?.takeIf { it.isNotBlank() }

    private val _uiState = MutableStateFlow(
        AgentFormUiState(definition = AgentFormDefinitions.get(formType))
    )
    val uiState: StateFlow<AgentFormUiState> = _uiState.asStateFlow()

    init {
        prepareSubmissionId()
        loadLinkedProduct()
    }

    fun onFieldChange(key: String, raw: String) {
        val field = findField(key) ?: return
        val filtered = FormValidators.filterInput(field, raw)
        _uiState.update { state ->
            val nextValues = state.fieldValues + (key to filtered)
            val touched = state.touchedFields + key
            recompute(
                state.copy(
                    fieldValues = nextValues,
                    touchedFields = touched,
                    message = null
                )
            )
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
            recompute(
                state.copy(
                    fieldValues = nextValues,
                    touchedFields = touched,
                    message = null
                )
            )
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
                                uploadErrors = current.uploadErrors - slot.key,
                                message = null
                            )
                        )
                    }
                    uploadDocument(slot, localPath, submissionId)
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

    private suspend fun uploadDocument(slot: FormUploadSlot, localPath: String, submissionId: String) {
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

    fun goToNextStep() {
        val state = _uiState.value
        val steps = state.definition.steps
        if (steps.isEmpty()) return
        _uiState.update { recompute(it.copy(stepValidationAttempted = true, errorMessage = null)) }
        val latest = _uiState.value
        if (!isCurrentStepValid(latest)) {
            val error = firstStepError(latest) ?: "Please fix the highlighted fields"
            _uiState.update { it.copy(errorMessage = error) }
            return
        }
        val next = (latest.currentStepIndex + 1).coerceAtMost(steps.lastIndex)
        _uiState.update {
            it.copy(
                currentStepIndex = next,
                stepValidationAttempted = false,
                errorMessage = null,
                message = null
            )
        }
    }

    fun goToPreviousStep() {
        _uiState.update {
            it.copy(
                currentStepIndex = (it.currentStepIndex - 1).coerceAtLeast(0),
                stepValidationAttempted = false,
                errorMessage = null
            )
        }
    }

    fun goToStep(index: Int) {
        val last = _uiState.value.definition.steps.lastIndex
        if (last < 0) return
        _uiState.update {
            it.copy(
                currentStepIndex = index.coerceIn(0, last),
                stepValidationAttempted = false,
                errorMessage = null
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
        if (state.storeProduct?.isAvailable == false) {
            _uiState.update { it.copy(errorMessage = "This product is currently unavailable.") }
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
            val result = formSubmissionRepository.createAndSubmitForm(submission)
            result.fold(
                onSuccess = { submitted ->
                    val linkedProduct = _uiState.value.storeProduct
                    val payNote = if (submitted.requiresPayment || submitted.status == FormSubmissionStatus.AWAITING_PAYMENT) {
                        " Pay GHS ${"%.2f".format(submitted.paymentAmount)} from My Submissions to complete this service."
                    } else {
                        ""
                    }
                    if (linkedProduct == null) {
                        _uiState.update { current ->
                            current.resetForm("Application submitted successfully.$payNote")
                        }
                        prepareSubmissionId()
                    }
                    linkedProduct
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = e.message ?: "Submission failed"
                        )
                    }
                    null
                }
            )?.let { linkedProduct ->
                placeLinkedProductOrder(
                    profileUserId = profile.userId,
                    agentName = profile.fullName,
                    product = linkedProduct,
                    formValues = normalizedData,
                    profilePhone = profile.phoneNumber,
                    profileLocation = profile.location
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }

    private fun prepareSubmissionId() {
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

    private fun loadLinkedProduct() {
        val id = productId ?: return
        viewModelScope.launch {
            val product = productRepository.getProduct(id)
            _uiState.update { it.copy(storeProduct = product) }
        }
    }

    private suspend fun placeLinkedProductOrder(
        profileUserId: String,
        agentName: String,
        product: StoreProduct,
        formValues: Map<String, String>,
        profilePhone: String,
        profileLocation: String
    ) {
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(
            phoneFromForm(formValues).ifBlank { profilePhone }
        )
        val location = locationFromForm(formValues).ifBlank { profileLocation }.trim()
        if (!PhoneNumberFormatter.isValidGhanaNumber(phone) || location.isBlank()) {
            finishStoreSubmit(
                product,
                "Application submitted. Add a valid phone and location to complete payment for ${product.productName}."
            )
            return
        }
        storeRepository.createAndSubmitOrder(
            userId = profileUserId,
            productId = product.productId,
            agentName = agentName,
            itemName = product.productName,
            quantity = 1,
            unitPrice = product.price,
            phoneNumber = phone,
            location = location
        ).onSuccess { order ->
            finishStoreSubmit(
                product,
                "Application submitted. Order ${order.status.firestoreValue} for ${product.productName} — 1 item, GHS ${"%.2f".format(product.price)}."
            )
        }.onFailure { e ->
            finishStoreSubmit(
                product,
                "Application submitted. Payment could not be completed: ${e.message ?: "try again from Store"}."
            )
        }
    }

    private fun finishStoreSubmit(product: StoreProduct, message: String) {
        _uiState.update { current ->
            current.resetForm(message).copy(storeProduct = product)
        }
        prepareSubmissionId()
    }

    private fun phoneFromForm(values: Map<String, String>): String =
        listOf("phoneNumber", "whatsappNumber")
            .map { values[it].orEmpty().trim() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

    private fun locationFromForm(values: Map<String, String>): String =
        listOf("location", "locationOfBirth")
            .map { values[it].orEmpty().trim() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

    private fun findField(key: String): FormTextField? =
        _uiState.value.definition.allFields().find { it.key == key }

    private fun visibleFields(state: AgentFormUiState): List<FormTextField> =
        state.definition.allFields().filter { it.isVisible(state.fieldValues) }

    private fun currentStepFields(state: AgentFormUiState): List<FormTextField> {
        val definition = state.definition
        if (!definition.isStepped()) return visibleFields(state)
        val step = definition.steps.getOrNull(state.currentStepIndex) ?: return visibleFields(state)
        if (step.isReview) return emptyList()
        val sectionFields = definition.sectionsForStep(state.currentStepIndex).flatMap { it.fields }
        val standalone = if (definition.includeStandaloneForStep(state.currentStepIndex)) {
            definition.standaloneFields
        } else {
            emptyList()
        }
        return (standalone + sectionFields).filter { it.isVisible(state.fieldValues) }
    }

    private fun currentStepUploads(state: AgentFormUiState): List<FormUploadSlot> {
        val definition = state.definition
        if (!definition.isStepped()) return definition.allUploads()
        val step = definition.steps.getOrNull(state.currentStepIndex) ?: return definition.allUploads()
        if (step.isReview) return emptyList()
        val sectionUploads = definition.sectionsForStep(state.currentStepIndex).flatMap { it.uploads }
        val standalone = if (definition.includeStandaloneForStep(state.currentStepIndex)) {
            definition.standaloneUploads
        } else {
            emptyList()
        }
        return standalone + sectionUploads
    }

    private fun isCurrentStepValid(state: AgentFormUiState): Boolean {
        val fieldsValid = currentStepFields(state).all { field ->
            val required = field.isEffectivelyRequired(state.fieldValues)
            FormValidators.isFieldValid(
                field.copy(required = required),
                state.fieldValues[field.key].orEmpty()
            )
        }
        val uploadsValid = currentStepUploads(state).all { slot ->
            FormValidators.isUploadedDocumentValid(
                slot,
                state.uploads[slot.key],
                slot.key in state.uploadingSlots
            )
        }
        val educationOk = FormValidators.educationDateOrderError(state.fieldValues).let { error ->
            error == null || currentStepFields(state).none { it.key == "schoolYearEnded" }
        }
        return fieldsValid && uploadsValid && educationOk
    }

    private fun firstStepError(state: AgentFormUiState): String? {
        currentStepFields(state).forEach { field ->
            val required = field.isEffectivelyRequired(state.fieldValues)
            val error = FormValidators.validateField(
                field.copy(required = required),
                state.fieldValues[field.key].orEmpty()
            )
            if (error != null) return error
        }
        currentStepUploads(state).forEach { slot ->
            val error = FormValidators.validateUploadedDocument(
                slot,
                state.uploads[slot.key],
                slot.key in state.uploadingSlots
            )
            if (error != null) return error
        }
        return FormValidators.educationDateOrderError(state.fieldValues)
    }

    private fun buildNormalizedFieldValues(state: AgentFormUiState): Map<String, String> =
        state.definition.allFields().associate { field ->
            if (!field.isVisible(state.fieldValues)) {
                field.key to ""
            } else {
                field.key to FormValidators.normalizeField(field, state.fieldValues[field.key].orEmpty())
            }
        }

    private fun recompute(state: AgentFormUiState): AgentFormUiState {
        val def = state.definition
        val fieldErrors = mutableMapOf<String, String>()
        val fieldValid = mutableMapOf<String, Boolean>()
        def.allFields().forEach { field ->
            val value = state.fieldValues[field.key].orEmpty()
            val visible = field.isVisible(state.fieldValues)
            val required = field.isEffectivelyRequired(state.fieldValues)
            val error = if (visible) {
                FormValidators.validateField(field.copy(required = required), value)
            } else {
                null
            }
            val show = visible && shouldShowFieldValidation(state, field.key, value)
            if (show && error != null) fieldErrors[field.key] = error
            fieldValid[field.key] = !visible || FormValidators.isFieldValid(field.copy(required = required), value)
        }
        FormValidators.educationDateOrderError(state.fieldValues)?.let { educationError ->
            if (shouldShowFieldValidation(state, "schoolYearEnded", state.fieldValues["schoolYearEnded"].orEmpty())) {
                fieldErrors["schoolYearEnded"] = educationError
            }
            fieldValid["schoolYearEnded"] = false
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
            visibleFields(state).all { field ->
                FormValidators.isFieldValid(
                    field.copy(required = field.isEffectivelyRequired(state.fieldValues)),
                    state.fieldValues[field.key].orEmpty()
                )
            } &&
            FormValidators.educationDateOrderError(state.fieldValues) == null &&
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

    private fun shouldShowFieldValidation(state: AgentFormUiState, key: String, value: String): Boolean {
        val inCurrentStep = currentStepFields(state).any { it.key == key }
        return state.validationAttempted ||
            (state.stepValidationAttempted && inCurrentStep) ||
            key in state.touchedFields ||
            value.isNotBlank()
    }

    private fun shouldShowUploadValidation(state: AgentFormUiState, key: String, path: String?): Boolean =
        state.validationAttempted || key in state.touchedUploads || !path.isNullOrBlank()
}
