package com.momokotle.agent.presentation.profit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.ProfitRuleRepository
import com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitResult
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.utils.AmountFormatter
import com.momokotle.agent.utils.PercentFormatter
import com.momokotle.agent.utils.ProfitAmountMatcher
import com.momokotle.agent.models.ProfitDefaults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DraftProfitRule(
    val minAmount: String = "",
    val maxAmount: String = "",
    val profitPercent: String = "",
    val fixedCommission: String = "",
    val useFixedCommission: Boolean = false
)

data class ProfitTypeSettingsUiState(
    val calculationMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
    val defaultPercentInput: String = "",
    val defaultFixedInput: String = "",
    val savedRules: List<ProfitRule> = emptyList(),
    val draftRule: DraftProfitRule = DraftProfitRule(),
    val pendingRules: List<ProfitRule> = emptyList(),
    val message: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class CashInSettingsViewModel @Inject constructor(
    settingsDataStore: SettingsDataStore,
    profitRuleRepository: ProfitRuleRepository
) : ProfitTypeSettingsViewModel(settingsDataStore, profitRuleRepository, ProfitRuleType.CASH_IN)

@HiltViewModel
class CashOutSettingsViewModel @Inject constructor(
    settingsDataStore: SettingsDataStore,
    profitRuleRepository: ProfitRuleRepository
) : ProfitTypeSettingsViewModel(settingsDataStore, profitRuleRepository, ProfitRuleType.CASH_OUT)

abstract class ProfitTypeSettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val profitRuleRepository: ProfitRuleRepository,
    private val ruleType: ProfitRuleType
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfitTypeSettingsUiState())
    val uiState: StateFlow<ProfitTypeSettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.settings.collect { settings ->
                val mode = when (ruleType) {
                    ProfitRuleType.CASH_IN -> settings.cashInCalculationMode
                    ProfitRuleType.CASH_OUT -> settings.cashOutCalculationMode
                }
                val defaultRate = when (ruleType) {
                    ProfitRuleType.CASH_IN -> settings.defaultCashInPercentage
                    ProfitRuleType.CASH_OUT -> settings.defaultCashOutPercentage
                }
                val defaultFixed = when (ruleType) {
                    ProfitRuleType.CASH_IN -> settings.defaultCashInFixedCommission
                    ProfitRuleType.CASH_OUT -> settings.defaultCashOutFixedCommission
                }
                _uiState.update {
                    it.copy(
                        calculationMode = mode,
                        defaultPercentInput = PercentFormatter.toDisplayPercent(defaultRate),
                        defaultFixedInput = formatFixed(defaultFixed)
                    )
                }
            }
        }
        viewModelScope.launch {
            profitRuleRepository.observeCustomRules(ruleType).collect { rules ->
                _uiState.update {
                    it.copy(
                        savedRules = rules,
                        pendingRules = if (it.pendingRules.isEmpty()) rules else it.pendingRules
                    )
                }
            }
        }
    }

    fun setCalculationMode(mode: ProfitCalculationMode) {
        viewModelScope.launch {
            when (ruleType) {
                ProfitRuleType.CASH_IN -> settingsDataStore.updateCashInCalculationMode(mode)
                ProfitRuleType.CASH_OUT -> settingsDataStore.updateCashOutCalculationMode(mode)
            }
            _uiState.update { it.copy(calculationMode = mode, message = "Mode saved") }
        }
    }

    fun onDefaultPercentChange(value: String) {
        _uiState.update { it.copy(defaultPercentInput = value) }
    }

    fun onDefaultFixedChange(value: String) {
        _uiState.update { it.copy(defaultFixedInput = value) }
    }

    fun saveDefaultPercent() {
        val rate = PercentFormatter.parsePercentInput(_uiState.value.defaultPercentInput)
        if (rate == null) {
            _uiState.update { it.copy(errorMessage = "Enter a valid percentage (e.g. 1 or 1.5)") }
            return
        }
        viewModelScope.launch {
            val display = PercentFormatter.toDisplayPercent(rate)
            when (ruleType) {
                ProfitRuleType.CASH_IN -> settingsDataStore.updateDefaultCashInPercent(display)
                ProfitRuleType.CASH_OUT -> settingsDataStore.updateDefaultCashOutPercent(display)
            }
            _uiState.update {
                it.copy(defaultPercentInput = display, message = "Default percentage saved", errorMessage = null)
            }
        }
    }

    fun saveDefaultFixed() {
        val fixed = AmountFormatter.parse(_uiState.value.defaultFixedInput)
        if (fixed == null || fixed < 0) {
            _uiState.update { it.copy(errorMessage = "Enter a valid fixed commission amount") }
            return
        }
        viewModelScope.launch {
            val display = formatFixed(fixed)
            when (ruleType) {
                ProfitRuleType.CASH_IN -> settingsDataStore.updateDefaultCashInFixed(display)
                ProfitRuleType.CASH_OUT -> settingsDataStore.updateDefaultCashOutFixed(display)
            }
            _uiState.update {
                it.copy(defaultFixedInput = display, message = "Fixed commission saved", errorMessage = null)
            }
        }
    }

    fun onDraftMinChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(minAmount = value)) }
    }

    fun onDraftMaxChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(maxAmount = value)) }
    }

    fun onDraftProfitPercentChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(profitPercent = value)) }
    }

    fun onDraftFixedCommissionChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(fixedCommission = value)) }
    }

    fun setDraftUseFixedCommission(useFixed: Boolean) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(useFixedCommission = useFixed)) }
    }

    fun addRule() {
        val minParsed = AmountFormatter.parse(_uiState.value.draftRule.minAmount)
        if (minParsed == null) {
            _uiState.update { it.copy(errorMessage = "Enter a valid minimum amount") }
            return
        }
        val maxParsed = AmountFormatter.parse(_uiState.value.draftRule.maxAmount) ?: minParsed
        if (maxParsed < minParsed) {
            _uiState.update { it.copy(errorMessage = "Maximum must be ≥ minimum") }
            return
        }
        val useFixed = _uiState.value.draftRule.useFixedCommission
        val fixed = if (useFixed) AmountFormatter.parse(_uiState.value.draftRule.fixedCommission) else null
        val percentRate = if (!useFixed) {
            PercentFormatter.parsePercentInput(_uiState.value.draftRule.profitPercent)
        } else null
        when {
            useFixed && fixed == null -> _uiState.update {
                it.copy(errorMessage = "Enter a valid fixed commission")
            }
            !useFixed && percentRate == null -> _uiState.update {
                it.copy(errorMessage = "Enter a valid percentage")
            }
            overlapsExisting(minParsed, maxParsed) -> _uiState.update {
                it.copy(errorMessage = "This range overlaps an existing tier")
            }
            else -> {
                val newRule = ProfitRule(
                    ruleType = ruleType,
                    minAmount = ProfitAmountMatcher.normalize(minParsed),
                    maxAmount = ProfitAmountMatcher.normalize(maxParsed),
                    fixedCommission = fixed ?: 0.0,
                    profitPercentage = percentRate ?: 0.0,
                    useFixedCommission = useFixed
                )
                _uiState.update {
                    it.copy(
                        pendingRules = (it.pendingRules + newRule).sortedBy { r -> r.minAmount },
                        draftRule = DraftProfitRule(),
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun overlapsExisting(min: Double, max: Double): Boolean {
        val nMin = ProfitAmountMatcher.normalize(min)
        val nMax = ProfitAmountMatcher.normalize(max)
        return _uiState.value.pendingRules.any { existing ->
            nMin <= existing.maxAmount && nMax >= existing.minAmount
        }
    }

    fun removePendingRule(rule: ProfitRule) {
        _uiState.update {
            it.copy(pendingRules = it.pendingRules.filterNot { r -> r.id == rule.id && r.minAmount == rule.minAmount })
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            profitRuleRepository.replaceCustomRules(ruleType, _uiState.value.pendingRules)
            _uiState.update { it.copy(message = "Settings saved", errorMessage = null) }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }

    private fun formatFixed(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
}

data class ProfitCalculationUiState(
    val cashInAmount: String = "",
    val cashOutAmount: String = "",
    val cashInResult: ProfitResult? = null,
    val cashOutResult: ProfitResult? = null,
    val cashInMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
    val cashOutMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE
)

@HiltViewModel
class ProfitCalculationViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val calculateVendorProfitUseCase: CalculateVendorProfitUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfitCalculationUiState())
    val uiState: StateFlow<ProfitCalculationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.settings.collect { settings ->
                _uiState.update {
                    it.copy(
                        cashInMode = settings.cashInCalculationMode,
                        cashOutMode = settings.cashOutCalculationMode
                    )
                }
            }
        }
    }

    fun onCashInAmountChange(value: String) {
        _uiState.update { it.copy(cashInAmount = value, cashInResult = null) }
    }

    fun onCashOutAmountChange(value: String) {
        _uiState.update { it.copy(cashOutAmount = value, cashOutResult = null) }
    }

    fun previewCashIn() {
        val amount = AmountFormatter.parse(_uiState.value.cashInAmount)
        if (amount == null) {
            _uiState.update { it.copy(cashInResult = ProfitResult(0.0, "Enter a valid amount")) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(cashInResult = calculateVendorProfitUseCase.calculateCashIn(amount)) }
        }
    }

    fun previewCashOut() {
        val amount = AmountFormatter.parse(_uiState.value.cashOutAmount)
        if (amount == null) {
            _uiState.update { it.copy(cashOutResult = ProfitResult(0.0, "Enter a valid amount")) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(cashOutResult = calculateVendorProfitUseCase.calculateCashOut(amount)) }
        }
    }
}
