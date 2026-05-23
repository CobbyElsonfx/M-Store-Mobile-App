package com.momokotle.agent.presentation.custommenu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.presentation.common.BaseUssdViewModel
import com.momokotle.agent.ussd.CustomFlowMapper
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.utils.AmountFormatter
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
    val ussdCode: String = "*171#",
    val step1: String = "",
    val step2: String = "",
    val step3: String = "",
    val useDynamicPhone: Boolean = false,
    val useDynamicAmount: Boolean = false,
    val phoneNumber: String = "",
    val amount: String = ""
)

@HiltViewModel
class CustomFlowViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startUssdFlowUseCase: StartUssdFlowUseCase,
    automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    private val customFlowRepository: CustomFlowRepository,
    private val recordAttemptedTransactionUseCase: RecordAttemptedTransactionUseCase
) : BaseUssdViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore) {

    val customFlows: StateFlow<List<CustomFlow>> = customFlowRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _formState = MutableStateFlow(CustomFlowFormState())
    val formState: StateFlow<CustomFlowFormState> = _formState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    fun onNameChange(v: String) = _formState.update { it.copy(name = v) }
    fun onUssdCodeChange(v: String) = _formState.update { it.copy(ussdCode = v) }
    fun onStep1Change(v: String) = _formState.update { it.copy(step1 = v) }
    fun onStep2Change(v: String) = _formState.update { it.copy(step2 = v) }
    fun onStep3Change(v: String) = _formState.update { it.copy(step3 = v) }
    fun onPhoneChange(v: String) = _formState.update { it.copy(phoneNumber = v) }
    fun onAmountChange(v: String) = _formState.update { it.copy(amount = v) }
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

    fun runFlow(flow: CustomFlow) {
        val form = _formState.value
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(form.phoneNumber)
        val amount = AmountFormatter.parse(form.amount)

        val needsPhone = flow.useDynamicPhone || flow.step1.equals("phone", true) ||
            flow.step2.equals("phone", true) || flow.step3.equals("phone", true)
        val needsAmount = flow.useDynamicAmount || flow.step1.equals("amount", true) ||
            flow.step2.equals("amount", true) || flow.step3.equals("amount", true)

        if (needsPhone && !PhoneNumberFormatter.isValidGhanaNumber(phone)) {
            _uiState.update { it.copy(errorMessage = "Enter a valid phone number to run this flow") }
            return
        }
        if (needsAmount && amount == null) {
            _uiState.update { it.copy(errorMessage = "Enter a valid amount to run this flow") }
            return
        }

        val ussdFlow = CustomFlowMapper.toUssdFlow(flow)
        viewModelScope.launch {
            val txId = recordAttemptedTransactionUseCase(
                type = TransactionType.CUSTOM,
                flowName = flow.name,
                amount = amount ?: 0.0,
                phoneNumber = phone.takeIf { needsPhone }
            )
            launchUssdFlow(
                flow = ussdFlow,
                phoneNumber = if (needsPhone) phone else "",
                amount = amount?.toInt()?.toString() ?: "",
                transactionId = txId
            )
        }
    }
}
