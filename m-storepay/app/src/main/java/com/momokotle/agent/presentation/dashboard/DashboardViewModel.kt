package com.momokotle.agent.presentation.dashboard

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.repository.BusinessSettingsRepository
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.SystemUssdFlowRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.domain.usecase.CheckDuplicateTransactionUseCase
import com.momokotle.agent.domain.usecase.CommissionPeriod
import com.momokotle.agent.domain.usecase.ObserveCommissionSummaryUseCase
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase
import com.momokotle.agent.domain.usecase.PromoteFrequentCustomerContactUseCase
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase
import com.momokotle.agent.domain.usecase.SearchCustomerSuggestionsUseCase
import com.momokotle.agent.domain.ussd.LargeTransactionWarning
import com.momokotle.agent.models.CommissionSummary
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.presentation.common.BaseUssdViewModel
import com.momokotle.agent.presentation.transactions.TransactionsViewModel
import com.momokotle.agent.ussd.CustomFlowExecutor
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdFlow
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
    val searchSuggestions: List<SavedContact> = emptyList()
)

data class LargeTransactionConfirmState(
    val type: TransactionType,
    val phoneNumber: String,
    val amount: Double,
    val launchPhone: String,
    val launchAmount: String,
    val flow: UssdFlow
)

data class DuplicateTransactionConfirmState(
    val prior: Transaction,
    val type: TransactionType,
    val launchPhone: String,
    val launchAmount: String,
    val amountValue: Double,
    val flow: UssdFlow
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext context: Context,
    startUssdFlowUseCase: StartUssdFlowUseCase,
    automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    accessibilityRepository: AccessibilityRepository,
    checkUssdAccessUseCase: CheckUssdAccessUseCase,
    cashOutSmsWatcher: com.momokotle.agent.sms.CashOutSmsWatcher,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val recordAttemptedTransactionUseCase: RecordAttemptedTransactionUseCase,
    transactionRepository: TransactionRepository,
    observeCommissionSummaryUseCase: ObserveCommissionSummaryUseCase,
    private val customFlowRepository: CustomFlowRepository,
    private val systemUssdFlowRepository: SystemUssdFlowRepository,
    private val savedContactRepository: SavedContactRepository,
    private val searchCustomerSuggestionsUseCase: SearchCustomerSuggestionsUseCase,
    private val promoteFrequentCustomerContactUseCase: PromoteFrequentCustomerContactUseCase,
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

    private val _formState = MutableStateFlow(DashboardFormState())
    val formState: StateFlow<DashboardFormState> = _formState.asStateFlow()

    private val _contactUiState = MutableStateFlow(DashboardContactUiState())
    val contactUiState: StateFlow<DashboardContactUiState> = _contactUiState.asStateFlow()

    private val _largeTransactionConfirm = MutableStateFlow<LargeTransactionConfirmState?>(null)
    val largeTransactionConfirm: StateFlow<LargeTransactionConfirmState?> =
        _largeTransactionConfirm.asStateFlow()

    private val _duplicateTransactionConfirm =
        MutableStateFlow<DuplicateTransactionConfirmState?>(null)
    val duplicateTransactionConfirm: StateFlow<DuplicateTransactionConfirmState?> =
        _duplicateTransactionConfirm.asStateFlow()

    val lastTransaction: StateFlow<Transaction?> = transactionRepository.observeRecent(1)
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val todayCommissionSummary: StateFlow<CommissionSummary> =
        observeCommissionSummaryUseCase.observe(CommissionPeriod.TODAY)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CommissionSummary())

    private var searchJob: Job? = null

    init {
        viewModelScope.launch { systemUssdFlowRepository.refreshFlows() }
    }

    fun refreshUssdFlows() {
        viewModelScope.launch { systemUssdFlowRepository.refreshFlows() }
    }

    fun refreshSubscriptionFromServer() {
        viewModelScope.launch { refreshUserProfileUseCase() }
    }

    fun onPhoneChange(value: String) {
        val phone = PhoneNumberFormatter.filterInput(value)
        _formState.update { it.copy(phoneNumber = phone) }
        scheduleContactSearch(phone)
    }

    fun onAmountChange(value: String) {
        _formState.update { it.copy(amount = value) }
    }

    /** Clears phone and amount (and related form extras) in one tap. */
    fun clearFormFields() {
        searchJob?.cancel()
        _formState.value = DashboardFormState()
        _contactUiState.update { it.copy(searchSuggestions = emptyList()) }
    }

    fun onPortedNumberChange(checked: Boolean) {
        _formState.update { it.copy(isPortedNumber = checked) }
    }

    fun onNetworkChange(network: ContactNetwork) {
        _formState.update { it.copy(network = network) }
    }

    fun selectContact(contact: SavedContact) {
        viewModelScope.launch {
            if (contact.id == 0L) {
                // Frequent history suggestion — persist now that the agent picked it.
                promoteFrequentCustomerContactUseCase(contact.phoneNumber)
            } else {
                savedContactRepository.updateContact(
                    contact.copy(updatedAt = System.currentTimeMillis())
                )
            }
        }
        _formState.update {
            it.copy(
                phoneNumber = PhoneNumberFormatter.normalizeGhanaNumber(contact.phoneNumber),
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
            val results = searchCustomerSuggestionsUseCase(trimmed)
            _contactUiState.update { it.copy(searchSuggestions = results) }
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

    fun startCashIn() = startAdminFlow(TransactionType.MTN_CASHIN)

    fun startCashOut() = startAdminFlow(TransactionType.MTN_CASHOUT)

    fun startPayToMerchant() = startAdminFlow(TransactionType.AGENT_MERCHANT)

    fun startTransferAirtime() = startAdminFlow(TransactionType.MTN_AIRTIME)

    fun startBalance() = startAdminFlow(TransactionType.BALANCE)

    fun startCommission() = startAdminFlow(TransactionType.COMMISSION)

    fun startEvdJust4u() = startAdminFlow(TransactionType.EVD_JUST4U)

    /**
     * Runs a custom menu from the dashboard using the phone/amount fields already
     * on screen (same pattern as Cash In / Cash Out). Does not open the
     * separate Custom Menu execution screen.
     */
    fun runCustomFlowFromDashboard(flowId: Long) {
        viewModelScope.launch {
            val custom = customFlowRepository.getById(flowId) ?: return@launch
            val rawPhone = _formState.value.phoneNumber
            val rawAmount = _formState.value.amount

            val validationError = CustomFlowExecutor.validateForRun(
                flow = custom,
                rawPhone = rawPhone,
                rawAmount = rawAmount
            )
            if (validationError != null) {
                _uiState.update { it.copy(errorMessage = validationError) }
                return@launch
            }

            val launchPhone = CustomFlowExecutor.phoneForLaunch(custom, rawPhone)
            val launchAmount = CustomFlowExecutor.amountForLaunch(custom, rawAmount)
            val ussdFlow = CustomFlowExecutor.buildUssdFlow(custom, rawPhone, rawAmount)
            val amountValue = CustomFlowExecutor.amountForTransaction(custom, rawAmount)

            if (CustomFlowExecutor.requiresAmount(custom) &&
                LargeTransactionWarning.shouldWarn(amountValue, largeTxThresholdGhs())
            ) {
                _largeTransactionConfirm.value = LargeTransactionConfirmState(
                    type = TransactionType.CUSTOM,
                    phoneNumber = launchPhone,
                    amount = amountValue,
                    launchPhone = launchPhone,
                    launchAmount = launchAmount,
                    flow = ussdFlow
                )
                return@launch
            }

            maybeWarnDuplicateThenProceed(
                type = TransactionType.CUSTOM,
                flow = ussdFlow,
                launchPhone = launchPhone,
                launchAmount = launchAmount,
                amountValue = amountValue
            )
        }
    }

    /** @deprecated Prefer [runCustomFlowFromDashboard]; kept for any no-input immediate runs. */
    fun runCustomFlowImmediately(flowId: Long) = runCustomFlowFromDashboard(flowId)

    private fun startAdminFlow(type: TransactionType) {
        viewModelScope.launch {
            val flow = systemUssdFlowRepository.getFlow(type)
            val phone = PhoneNumberFormatter.normalizeGhanaNumber(_formState.value.phoneNumber)
            val amount = AmountFormatter.parse(_formState.value.amount)

            when {
                flow.requiresPhone && !PhoneNumberFormatter.isValidGhanaNumber(phone) -> {
                    _uiState.update { it.copy(errorMessage = "Enter a valid phone number") }
                    return@launch
                }
                flow.requiresAmount && amount == null -> {
                    _uiState.update { it.copy(errorMessage = "Enter a valid amount") }
                    return@launch
                }
            }

            val launchPhone = if (flow.requiresPhone) phone else ""
            val launchAmount = if (flow.requiresAmount && amount != null) {
                AmountFormatter.formatForUssd(amount)
            } else {
                ""
            }

            val amountValue = if (flow.requiresAmount) amount ?: 0.0 else 0.0

            if (flow.requiresAmount &&
                LargeTransactionWarning.shouldWarn(amount, largeTxThresholdGhs())
            ) {
                _largeTransactionConfirm.value = LargeTransactionConfirmState(
                    type = type,
                    phoneNumber = launchPhone,
                    amount = amount ?: 0.0,
                    launchPhone = launchPhone,
                    launchAmount = launchAmount,
                    flow = flow
                )
                return@launch
            }

            maybeWarnDuplicateThenProceed(
                type = type,
                flow = flow,
                launchPhone = launchPhone,
                launchAmount = launchAmount,
                amountValue = amountValue
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
                type = pending.type,
                flow = pending.flow,
                launchPhone = pending.launchPhone,
                launchAmount = pending.launchAmount,
                amountValue = pending.amount
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
            proceedAdminFlow(
                type = pending.type,
                flow = pending.flow,
                launchPhone = pending.launchPhone,
                launchAmount = pending.launchAmount,
                amountValue = pending.amountValue
            )
        }
    }

    private suspend fun maybeWarnDuplicateThenProceed(
        type: TransactionType,
        flow: UssdFlow,
        launchPhone: String,
        launchAmount: String,
        amountValue: Double
    ) {
        // Use launchPhone (covers DIRECT custom menus where UssdFlow has no phone step).
        if (launchPhone.isNotBlank()) {
            val prior = checkDuplicateTransactionUseCase(launchPhone)
            if (prior != null) {
                _duplicateTransactionConfirm.value = DuplicateTransactionConfirmState(
                    prior = prior,
                    type = type,
                    launchPhone = launchPhone,
                    launchAmount = launchAmount,
                    amountValue = amountValue,
                    flow = flow
                )
                return
            }
        }
        proceedAdminFlow(
            type = type,
            flow = flow,
            launchPhone = launchPhone,
            launchAmount = launchAmount,
            amountValue = amountValue
        )
    }

    private suspend fun proceedAdminFlow(
        type: TransactionType,
        flow: UssdFlow,
        launchPhone: String,
        launchAmount: String,
        amountValue: Double
    ) {
        val txId = recordAttemptedTransactionUseCase(
            type = type,
            flowName = flow.name,
            amount = amountValue,
            phoneNumber = launchPhone.ifBlank { null }
        )
        launchUssdFlow(
            flow = flow,
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

    fun typeLabel(type: TransactionType): String = TransactionsViewModel.typeLabel(type)
}
