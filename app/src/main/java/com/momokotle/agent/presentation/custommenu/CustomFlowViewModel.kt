package com.momokotle.agent.presentation.custommenu

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.CustomMenuType
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.presentation.common.BaseUssdViewModel
import com.momokotle.agent.ussd.CustomFlowExecutor
import com.momokotle.agent.ussd.UssdAutomationEngine
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
    val ussdCode: String = "*171#",
    val step1: String = "",
    val step2: String = "",
    val step3: String = "",
    val useDynamicPhone: Boolean = false,
    val useDynamicAmount: Boolean = false
)

data class CustomMenuExecutionUiState(
    val flow: CustomFlow? = null,
    val menuType: CustomMenuType? = null,
    val requiresPhone: Boolean = false,
    val requiresAmount: Boolean = false,
    val phoneNumber: String = "",
    val amount: String = "",
    val loadError: String? = null
)

@HiltViewModel
class CustomFlowViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startUssdFlowUseCase: StartUssdFlowUseCase,
    automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    accessibilityRepository: AccessibilityRepository,
    private val customFlowRepository: CustomFlowRepository,
    private val recordAttemptedTransactionUseCase: RecordAttemptedTransactionUseCase
) : BaseUssdViewModel(
    context,
    startUssdFlowUseCase,
    automationEngine,
    settingsDataStore,
    accessibilityRepository
) {

    val customFlows: StateFlow<List<CustomFlow>> = customFlowRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _formState = MutableStateFlow(CustomFlowFormState())
    val formState: StateFlow<CustomFlowFormState> = _formState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _executionState = MutableStateFlow(CustomMenuExecutionUiState())
    val executionState: StateFlow<CustomMenuExecutionUiState> = _executionState.asStateFlow()

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
        _executionState.update { it.copy(phoneNumber = value) }
    }

    fun onExecutionAmountChange(value: String) {
        _executionState.update { it.copy(amount = value) }
    }

    fun runExecution() {
        val flow = _executionState.value.flow ?: return
        val exec = _executionState.value

        val validationError = CustomFlowExecutor.validateForRun(
            flow = flow,
            rawPhone = exec.phoneNumber,
            rawAmount = exec.amount
        )
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        val phone = CustomFlowExecutor.phoneForLaunch(flow, exec.phoneNumber)
        val amountStr = CustomFlowExecutor.amountForLaunch(flow, exec.amount)
        val ussdFlow = CustomFlowExecutor.buildUssdFlow(flow, exec.phoneNumber, exec.amount)

        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
            val txId = recordAttemptedTransactionUseCase(
                type = TransactionType.CUSTOM,
                flowName = flow.name,
                amount = CustomFlowExecutor.amountForTransaction(flow, exec.amount),
                phoneNumber = CustomFlowExecutor.phoneForTransaction(flow, exec.phoneNumber)
            )
            launchUssdFlow(
                flow = ussdFlow,
                phoneNumber = phone,
                amount = amountStr,
                transactionId = txId
            )
        }
    }

    fun onNameChange(v: String) = _formState.update { it.copy(name = v) }
    fun onUssdCodeChange(v: String) = _formState.update { it.copy(ussdCode = v) }
    fun onStep1Change(v: String) = _formState.update { it.copy(step1 = v) }
    fun onStep2Change(v: String) = _formState.update { it.copy(step2 = v) }
    fun onStep3Change(v: String) = _formState.update { it.copy(step3 = v) }
    fun onDynamicPhoneChange(v: Boolean) = _formState.update { it.copy(useDynamicPhone = v) }
    fun onDynamicAmountChange(v: Boolean) = _formState.update { it.copy(useDynamicAmount = v) }

    fun startNewFlow() {
        _formState.value = CustomFlowFormState()
        _isEditing.value = true
    }

    fun editFlow(flow: CustomFlow) {
        _formState.value = CustomFlowFormState(
            id = flow.id,
            name = flow.name,
            ussdCode = flow.ussdCode,
            step1 = flow.step1,
            step2 = flow.step2,
            step3 = flow.step3,
            useDynamicPhone = flow.useDynamicPhone,
            useDynamicAmount = flow.useDynamicAmount
        )
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
        viewModelScope.launch {
            customFlowRepository.save(
                CustomFlow(
                    id = form.id,
                    name = form.name.trim(),
                    ussdCode = form.ussdCode.trim(),
                    step1 = form.step1.trim(),
                    step2 = form.step2.trim(),
                    step3 = form.step3.trim(),
                    useDynamicPhone = form.useDynamicPhone,
                    useDynamicAmount = form.useDynamicAmount
                )
            )
            cancelEdit()
            _uiState.update { it.copy(infoMessage = "Custom flow saved") }
        }
    }

    fun deleteFlow(id: Long) {
        viewModelScope.launch {
            customFlowRepository.delete(id)
        }
    }
}
