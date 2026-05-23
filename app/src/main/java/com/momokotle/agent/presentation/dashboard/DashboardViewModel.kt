package com.momokotle.agent.presentation.dashboard

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase
import com.momokotle.agent.domain.usecase.SaveContactResult
import com.momokotle.agent.domain.usecase.SaveContactUseCase
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.presentation.common.BaseUssdViewModel
import com.momokotle.agent.presentation.transactions.TransactionsViewModel
import com.momokotle.agent.ui.components.SaveContactFormState
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdFlows
import com.momokotle.agent.utils.AmountFormatter
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardFormState(
    val phoneNumber: String = "",
    val amount: String = "",
    val isPortedNumber: Boolean = false,
    val network: ContactNetwork = ContactNetwork.UNKNOWN
)

data class DashboardContactUiState(
    val searchSuggestions: List<SavedContact> = emptyList(),
    val showSaveDialog: Boolean = false,
    val saveDialogForm: SaveContactFormState = SaveContactFormState(),
    val saveDialogError: String? = null,
    val showPostTransactionSavePrompt: Boolean = false,
    val postTransactionPhone: String = ""
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startUssdFlowUseCase: StartUssdFlowUseCase,
    automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    private val recordAttemptedTransactionUseCase: RecordAttemptedTransactionUseCase,
    transactionRepository: TransactionRepository,
    private val savedContactRepository: SavedContactRepository,
    private val saveContactUseCase: SaveContactUseCase
) : BaseUssdViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore) {

    private val _formState = MutableStateFlow(DashboardFormState())
    val formState: StateFlow<DashboardFormState> = _formState.asStateFlow()

    private val _contactUiState = MutableStateFlow(DashboardContactUiState())
    val contactUiState: StateFlow<DashboardContactUiState> = _contactUiState.asStateFlow()

    val lastTransaction: StateFlow<Transaction?> = transactionRepository.observeRecent(1)
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private var searchJob: Job? = null

    fun onPhoneChange(value: String) {
        _formState.update { it.copy(phoneNumber = value) }
        scheduleContactSearch(value)
    }

    fun onAmountChange(value: String) {
        _formState.update { it.copy(amount = value) }
    }

    fun onPortedNumberChange(checked: Boolean) {
        _formState.update { it.copy(isPortedNumber = checked) }
    }

    fun onNetworkChange(network: ContactNetwork) {
        _formState.update { it.copy(network = network) }
    }

    fun selectContact(contact: SavedContact) {
        viewModelScope.launch {
            savedContactRepository.updateContact(
                contact.copy(updatedAt = System.currentTimeMillis())
            )
        }
        _formState.update {
            it.copy(
                phoneNumber = contact.phoneNumber,
                isPortedNumber = contact.isPorted,
                network = contact.network
            )
        }
        _contactUiState.update { it.copy(searchSuggestions = emptyList()) }
    }

    fun dismissSearchSuggestions() {
        _contactUiState.update { it.copy(searchSuggestions = emptyList()) }
    }

    private fun scheduleContactSearch(query: String) {
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.length < 2) {
            _contactUiState.update { it.copy(searchSuggestions = emptyList()) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(200)
            val results = savedContactRepository.searchContacts(trimmed)
            _contactUiState.update { it.copy(searchSuggestions = results) }
        }
    }

    fun openSaveContactDialog() {
        val phone = _formState.value.phoneNumber
        if (phone.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter a phone number first") }
            return
        }
        _contactUiState.update {
            it.copy(
                showSaveDialog = true,
                saveDialogForm = SaveContactFormState(
                    phoneNumber = phone,
                    network = _formState.value.network,
                    isPorted = _formState.value.isPortedNumber
                ),
                saveDialogError = null
            )
        }
    }

    fun dismissSaveDialog() {
        _contactUiState.update {
            it.copy(showSaveDialog = false, saveDialogError = null)
        }
    }

    fun onSaveDialogNameChange(value: String) {
        _contactUiState.update { state ->
            state.copy(saveDialogForm = state.saveDialogForm.copy(contactName = value))
        }
    }

    fun onSaveDialogPhoneChange(value: String) {
        _contactUiState.update { state ->
            state.copy(saveDialogForm = state.saveDialogForm.copy(phoneNumber = value))
        }
    }

    fun onSaveDialogNetworkChange(network: ContactNetwork) {
        _contactUiState.update { state ->
            state.copy(saveDialogForm = state.saveDialogForm.copy(network = network))
        }
    }

    fun onSaveDialogPortedChange(checked: Boolean) {
        _contactUiState.update { state ->
            state.copy(saveDialogForm = state.saveDialogForm.copy(isPorted = checked))
        }
    }

    fun saveContactFromDialog() {
        val form = _contactUiState.value.saveDialogForm
        viewModelScope.launch {
            when (val result = saveContactUseCase(
                contactName = form.contactName,
                phoneNumber = form.phoneNumber,
                network = form.network,
                isPorted = form.isPorted
            )) {
                is SaveContactResult.Success -> {
                    _formState.update {
                        it.copy(
                            phoneNumber = result.contact.phoneNumber,
                            isPortedNumber = result.contact.isPorted,
                            network = result.contact.network
                        )
                    }
                    _contactUiState.update {
                        it.copy(
                            showSaveDialog = false,
                            saveDialogError = null,
                            showPostTransactionSavePrompt = false
                        )
                    }
                    _uiState.update { it.copy(infoMessage = "Contact saved") }
                }
                is SaveContactResult.Error -> {
                    _contactUiState.update { it.copy(saveDialogError = result.message) }
                }
            }
        }
    }

    fun dismissPostTransactionSavePrompt() {
        _contactUiState.update {
            it.copy(showPostTransactionSavePrompt = false, postTransactionPhone = "")
        }
    }

    fun acceptPostTransactionSave() {
        val phone = _contactUiState.value.postTransactionPhone
        _contactUiState.update {
            it.copy(
                showPostTransactionSavePrompt = false,
                showSaveDialog = true,
                saveDialogForm = SaveContactFormState(
                    phoneNumber = phone,
                    network = _formState.value.network,
                    isPorted = _formState.value.isPortedNumber
                ),
                saveDialogError = null
            )
        }
    }

    fun applyPrefillContact(contact: SavedContact) {
        selectContact(contact)
    }

    fun repeatLastTransaction() {
        val last = lastTransaction.value ?: return
        _formState.update {
            it.copy(
                phoneNumber = last.phoneNumber.orEmpty(),
                amount = if (last.amount > 0) {
                    if (last.amount % 1.0 == 0.0) last.amount.toLong().toString() else last.amount.toString()
                } else {
                    ""
                }
            )
        }
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    fun startCashIn() = startDetailedFlow(TransactionType.MTN_CASHIN, UssdFlows.CashInFlow.name)

    fun startCashOut() = startDetailedFlow(TransactionType.MTN_CASHOUT, UssdFlows.CashOutFlow.name)

    fun startPayToMerchant() = startDetailedFlow(TransactionType.AGENT_MERCHANT, UssdFlows.PayToMerchantFlow.name)

    fun startTransferAirtime() = startDetailedFlow(TransactionType.MTN_AIRTIME, UssdFlows.TransferAirtimeFlow.name)

    fun startBalance() = startSimpleFlow(TransactionType.BALANCE, UssdFlows.BalanceFlow)

    fun startCommission() = startSimpleFlow(TransactionType.COMMISSION, UssdFlows.CommissionFlow)

    fun startEvdJust4u() = startSimpleFlow(TransactionType.EVD_JUST4U, UssdFlows.EvdJust4uFlow)

    private fun startDetailedFlow(type: TransactionType, flowName: String) {
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(_formState.value.phoneNumber)
        val amount = AmountFormatter.parse(_formState.value.amount)

        when {
            !PhoneNumberFormatter.isValidGhanaNumber(phone) -> {
                _uiState.update { it.copy(errorMessage = "Enter a valid phone number") }
                return
            }
            amount == null -> {
                _uiState.update { it.copy(errorMessage = "Enter a valid amount") }
                return
            }
        }

        viewModelScope.launch {
            val txId = recordAttemptedTransactionUseCase(
                type = type,
                flowName = flowName,
                amount = amount,
                phoneNumber = phone
            )
            launchUssdFlow(
                flow = UssdFlows.forTransactionType(type),
                phoneNumber = phone,
                amount = amount.toInt().toString(),
                transactionId = txId
            )
            maybePromptSaveContact(phone, type)
        }
    }

    private suspend fun maybePromptSaveContact(phone: String, type: TransactionType) {
        val promptTypes = setOf(
            TransactionType.MTN_CASHIN,
            TransactionType.MTN_CASHOUT,
            TransactionType.MTN_AIRTIME,
            TransactionType.AGENT_MERCHANT
        )
        if (type !in promptTypes) return
        if (savedContactRepository.existsByPhoneNumber(phone)) return
        _contactUiState.update {
            it.copy(
                showPostTransactionSavePrompt = true,
                postTransactionPhone = phone
            )
        }
    }

    private fun startSimpleFlow(type: TransactionType, flow: com.momokotle.agent.ussd.UssdFlow) {
        viewModelScope.launch {
            val txId = recordAttemptedTransactionUseCase(
                type = type,
                flowName = flow.name,
                amount = 0.0,
                phoneNumber = null
            )
            launchUssdFlow(flow = flow, transactionId = txId)
        }
    }

    fun typeLabel(type: TransactionType): String = TransactionsViewModel.typeLabel(type)
}
