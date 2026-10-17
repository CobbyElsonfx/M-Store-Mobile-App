package com.momokotle.agent.presentation.custommenu

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.repository.BusinessSettingsRepository
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.usecase.CheckDuplicateTransactionUseCase
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.domain.ussd.LargeTransactionWarning
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.CustomMenuType
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.presentation.common.BaseUssdViewModel
import com.momokotle.agent.ussd.CustomFlowExecutor
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomFlowFormState(
    val id: Long = 0,
    val name: String = "",
    val ussdCode: String = "",
    /** Editable step fields; may include blanks while typing. Cap: [CustomFlow.MAX_STEPS]. */
    val steps: List<StepDraft> = listOf(StepDraft()),
    val useDynamicPhone: Boolean = false,
    val useDynamicAmount: Boolean = false
) {
    val canAddStep: Boolean get() = steps.size < CustomFlow.MAX_STEPS
}

/** Stable identity so Compose TextFields keep the correct value when steps are added/removed. */
data class StepDraft(
    val id: Long = nextStepDraftId(),
    val text: String = ""
)

private val stepDraftIdSeq = java.util.concurrent.atomic.AtomicLong(1L)
private fun nextStepDraftId(): Long = stepDraftIdSeq.getAndIncrement()

data class CustomMenuExecutionUiState(
    val flow: CustomFlow? = null,
    val menuType: CustomMenuType? = null,
    val requiresPhone: Boolean = false,
    val requiresAmount: Boolean = false,
    val phoneNumber: String = "",
    val amount: String = "",
    val loadError: String? = null
)

data class CustomLargeTransactionConfirmState(
    val customFlow: CustomFlow,
    val rawPhone: String,
    val rawAmount: String,
    val amountValue: Double,
    val phoneForDisplay: String,
    val ussdFlow: UssdFlow,
    val launchPhone: String,
    val launchAmount: String
)

data class CustomDuplicateTransactionConfirmState(
    val prior: Transaction,
    val customFlow: CustomFlow,
    val rawPhone: String,
    val amountValue: Double,
    val ussdFlow: UssdFlow,
    val launchPhone: String,
    val launchAmount: String
)

@HiltViewModel
class CustomFlowViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startUssdFlowUseCase: StartUssdFlowUseCase,
    automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    accessibilityRepository: AccessibilityRepository,
    checkUssdAccessUseCase: CheckUssdAccessUseCase,
    cashOutSmsWatcher: com.momokotle.agent.sms.CashOutSmsWatcher,
    private val customFlowRepository: CustomFlowRepository,
    private val recordAttemptedTransactionUseCase: RecordAttemptedTransactionUseCase,
    private val businessSettingsRepository: BusinessSettingsRepository,
    private val checkDuplicateTransactionUseCase: CheckDuplicateTransactionUseCase
) : BaseUssdViewModel(
    context,
    startUssdFlowUseCase,
    automationEngine,
    settingsDataStore,
    accessibilityRepository,
    checkUssdAccessUseCase,
    cashOutSmsWatcher
) {

    val customFlows: StateFlow<List<CustomFlow>> = customFlowRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _formState = MutableStateFlow(CustomFlowFormState())
    val formState: StateFlow<CustomFlowFormState> = _formState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _executionState = MutableStateFlow(CustomMenuExecutionUiState())
    val executionState: StateFlow<CustomMenuExecutionUiState> = _executionState.asStateFlow()

    private val _largeTransactionConfirm = MutableStateFlow<CustomLargeTransactionConfirmState?>(null)
    val largeTransactionConfirm: StateFlow<CustomLargeTransactionConfirmState?> =
        _largeTransactionConfirm.asStateFlow()

    private val _duplicateTransactionConfirm =
        MutableStateFlow<CustomDuplicateTransactionConfirmState?>(null)
    val duplicateTransactionConfirm: StateFlow<CustomDuplicateTransactionConfirmState?> =
        _duplicateTransactionConfirm.asStateFlow()

    fun loadForExecution(flowId: Long) {
        if (flowId <= 0L) {
            _executionState.update { it.copy(loadError = "Invalid custom menu") }
            return
        }
        viewModelScope.launch {
            _executionState.value = CustomMenuExecutionUiState()
            val flow = customFlowRepository.getById(flowId)
            if (flow == null) {
                _executionState.update { it.copy(loadError = "Custom menu not found") }
                return@launch
            }
            _executionState.update {
                it.copy(
                    flow = flow,
                    menuType = CustomFlowExecutor.menuType(flow),
                    requiresPhone = CustomFlowExecutor.requiresPhone(flow),
                    requiresAmount = CustomFlowExecutor.requiresAmount(flow)
                )
            }
        }
    }

    fun onExecutionPhoneChange(value: String) {
        _executionState.update {
            it.copy(phoneNumber = PhoneNumberFormatter.filterInput(value))
        }
    }

    fun onExecutionAmountChange(value: String) {
        _executionState.update { it.copy(amount = value) }
    }

    fun runExecution() {
        val flow = _executionState.value.flow ?: return
        runFlowInternal(flow, _executionState.value.phoneNumber, _executionState.value.amount)
    }

    fun runCustomFlowImmediately(flowId: Long) {
        viewModelScope.launch {
            val flow = customFlowRepository.getById(flowId) ?: return@launch
            if (!CustomFlowExecutor.canRunImmediately(flow)) return@launch
            runFlowInternal(flow, "", "")
        }
    }

    private fun runFlowInternal(flow: CustomFlow, phoneNumber: String, amount: String) {
        val validationError = CustomFlowExecutor.validateForRun(
            flow = flow,
            rawPhone = phoneNumber,
            rawAmount = amount
        )
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        val phone = CustomFlowExecutor.phoneForLaunch(flow, phoneNumber)
        val amountStr = CustomFlowExecutor.amountForLaunch(flow, amount)
        val ussdFlow = CustomFlowExecutor.buildUssdFlow(flow, phoneNumber, amount)
        val amountValue = CustomFlowExecutor.amountForTransaction(flow, amount)

        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
            if (CustomFlowExecutor.requiresAmount(flow) &&
                LargeTransactionWarning.shouldWarn(amountValue, largeTxThresholdGhs())
            ) {
                _largeTransactionConfirm.value = CustomLargeTransactionConfirmState(
                    customFlow = flow,
                    rawPhone = phoneNumber,
                    rawAmount = amount,
                    amountValue = amountValue,
                    phoneForDisplay = phone,
                    ussdFlow = ussdFlow,
                    launchPhone = phone,
                    launchAmount = amountStr
                )
                return@launch
            }
            maybeWarnDuplicateThenProceed(
                flow = flow,
                ussdFlow = ussdFlow,
                launchPhone = phone,
                launchAmount = amountStr,
                amountValue = amountValue,
                phoneNumber = phoneNumber
            )
        }
    }

    fun dismissLargeTransactionConfirm() {
        _largeTransactionConfirm.value = null
    }

    fun confirmLargeTransaction() {
        val pending = _largeTransactionConfirm.value ?: return
        _largeTransactionConfirm.value = null
        viewModelScope.launch {
            maybeWarnDuplicateThenProceed(
                flow = pending.customFlow,
                ussdFlow = pending.ussdFlow,
                launchPhone = pending.launchPhone,
                launchAmount = pending.launchAmount,
                amountValue = pending.amountValue,
                phoneNumber = pending.rawPhone
            )
        }
    }

    fun dismissDuplicateTransactionConfirm() {
        _duplicateTransactionConfirm.value = null
    }

    fun confirmDuplicateTransaction() {
        val pending = _duplicateTransactionConfirm.value ?: return
        _duplicateTransactionConfirm.value = null
        viewModelScope.launch {
            proceedCustomFlow(
                flow = pending.customFlow,
                ussdFlow = pending.ussdFlow,
                launchPhone = pending.launchPhone,
                launchAmount = pending.launchAmount,
                amountValue = pending.amountValue,
                phoneNumber = pending.rawPhone
            )
        }
    }

    private suspend fun maybeWarnDuplicateThenProceed(
        flow: CustomFlow,
        ussdFlow: UssdFlow,
        launchPhone: String,
        launchAmount: String,
        amountValue: Double,
        phoneNumber: String
    ) {
        if (CustomFlowExecutor.requiresPhone(flow) && launchPhone.isNotBlank()) {
            val prior = checkDuplicateTransactionUseCase(launchPhone)
            if (prior != null) {
                _duplicateTransactionConfirm.value = CustomDuplicateTransactionConfirmState(
                    prior = prior,
                    customFlow = flow,
                    rawPhone = phoneNumber,
                    amountValue = amountValue,
                    ussdFlow = ussdFlow,
                    launchPhone = launchPhone,
                    launchAmount = launchAmount
                )
                return
            }
        }
        proceedCustomFlow(
            flow = flow,
            ussdFlow = ussdFlow,
            launchPhone = launchPhone,
            launchAmount = launchAmount,
            amountValue = amountValue,
            phoneNumber = phoneNumber
        )
    }

    private suspend fun proceedCustomFlow(
        flow: CustomFlow,
        ussdFlow: UssdFlow,
        launchPhone: String,
        launchAmount: String,
        amountValue: Double,
        phoneNumber: String
    ) {
        val txId = recordAttemptedTransactionUseCase(
            type = TransactionType.CUSTOM,
            flowName = flow.name,
            amount = amountValue,
            phoneNumber = CustomFlowExecutor.phoneForTransaction(flow, phoneNumber)
        )
        launchUssdFlow(
            flow = ussdFlow,
            phoneNumber = launchPhone,
            amount = launchAmount,
            transactionId = txId
        )
    }

    private suspend fun largeTxThresholdGhs(): Double =
        runCatching { businessSettingsRepository.getSettings() }
            .getOrDefault(BusinessSettings())
            .largeTransactionWarningAmount
            .takeIf { it > 0.0 }
            ?: LargeTransactionWarning.DEFAULT_THRESHOLD_GHS

    fun onNameChange(v: String) = _formState.update { it.copy(name = v) }
    fun onUssdCodeChange(v: String) = _formState.update { it.copy(ussdCode = v) }
    fun onDynamicPhoneChange(v: Boolean) = _formState.update { it.copy(useDynamicPhone = v) }
    fun onDynamicAmountChange(v: Boolean) = _formState.update { it.copy(useDynamicAmount = v) }

    fun onStepChange(index: Int, value: String) {
        _formState.update { state ->
            if (index !in state.steps.indices) return@update state
            state.copy(
                steps = state.steps.toMutableList().also { list ->
                    list[index] = list[index].copy(text = value)
                }
            )
        }
    }

    fun addStep() {
        _formState.update { state ->
            if (!state.canAddStep) return@update state
            state.copy(steps = state.steps + StepDraft())
        }
    }

    fun removeStep(index: Int) {
        _formState.update { state ->
            if (state.steps.size <= 1 || index !in state.steps.indices) return@update state
            state.copy(steps = state.steps.toMutableList().also { it.removeAt(index) })
        }
    }

    fun startNewFlow() {
        _formState.value = CustomFlowFormState()
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
        _isEditing.value = true
    }

    fun editFlow(flow: CustomFlow) {
        val existing = flow.normalizedSteps()
        _formState.value = CustomFlowFormState(
            id = flow.id,
            name = flow.name,
            ussdCode = flow.ussdCode,
            steps = if (existing.isEmpty()) {
                listOf(StepDraft())
            } else {
                existing.map { StepDraft(text = it) }
            },
            useDynamicPhone = flow.useDynamicPhone,
            useDynamicAmount = flow.useDynamicAmount
        )
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
        _isEditing.value = true
    }

    fun cancelEdit() {
        _isEditing.value = false
        _formState.value = CustomFlowFormState()
    }

    fun saveFlow() {
        val form = _formState.value
        if (form.name.isBlank() || form.ussdCode.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Menu name and USSD code are required") }
            return
        }
        val steps = form.steps
            .map { it.text.trim() }
            .filter { it.isNotEmpty() }
            .take(CustomFlow.MAX_STEPS)
        viewModelScope.launch {
            customFlowRepository.save(
                CustomFlow(
                    id = form.id,
                    name = form.name.trim(),
                    ussdCode = form.ussdCode.trim(),
                    steps = steps,
                    useDynamicPhone = form.useDynamicPhone,
                    useDynamicAmount = form.useDynamicAmount
                )
            )
            cancelEdit()
            _uiState.update { it.copy(infoMessage = "Custom flow saved", errorMessage = null) }
        }
    }

    fun deleteFlow(id: Long) {
        viewModelScope.launch {
            customFlowRepository.delete(id)
        }
    }
}
