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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DraftProfitRule(
    val amount: String = "",
    val profitPercent: String = ""
)

data class ProfitTypeSettingsUiState(
    val calculationMode: ProfitCalculationMode = ProfitCalculationMode.DEFAULT,
    val defaultPercentInput: String = "",
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
                _uiState.update {
                    it.copy(
                        calculationMode = mode,
                        defaultPercentInput = PercentFormatter.toDisplayPercent(defaultRate)
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
                it.copy(
                    defaultPercentInput = display,
                    message = "Default percentage saved",
                    errorMessage = null
                )
            }
        }
    }

    fun onDraftAmountChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(amount = value)) }
    }

    fun onDraftProfitPercentChange(value: String) {
        _uiState.update { it.copy(draftRule = it.draftRule.copy(profitPercent = value)) }
    }

    fun addRule() {
        val amount = AmountFormatter.parse(_uiState.value.draftRule.amount)
        val percentRate = PercentFormatter.parsePercentInput(_uiState.value.draftRule.profitPercent)
        when {
            amount == null -> _uiState.update { it.copy(errorMessage = "Enter a valid amount") }
            percentRate == null -> _uiState.update {
                it.copy(errorMessage = "Enter a valid profit percentage (e.g. 1 or 1.5)")
            }
            else -> {
                val normalizedAmount = ProfitAmountMatcher.normalize(amount)
                val duplicate = _uiState.value.pendingRules.any {
                    ProfitAmountMatcher.matches(it.amount, normalizedAmount)
                }
                if (duplicate) {
                    _uiState.update { it.copy(errorMessage = "Rule for this amount already exists") }
                    return
                }
                val newRule = ProfitRule(
                    ruleType = ruleType,
                    amount = normalizedAmount,
                    profitPercentage = percentRate
                )
                _uiState.update {
                    it.copy(
                        pendingRules = (it.pendingRules + newRule).sortedBy { r -> r.amount },
                        draftRule = DraftProfitRule(),
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun removePendingRule(amount: Double) {
        _uiState.update {
            it.copy(
                pendingRules = it.pendingRules.filterNot { rule ->
                    ProfitAmountMatcher.matches(rule.amount, amount)
                }
            )
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            profitRuleRepository.replaceCustomRules(ruleType, _uiState.value.pendingRules)
            _uiState.update {
                it.copy(message = "Settings saved", errorMessage = null)
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }
}

data class ProfitCalculationUiState(
    val cashInAmount: String = "",
    val cashOutAmount: String = "",
    val cashInResult: ProfitResult? = null,
    val cashOutResult: ProfitResult? = null,
    val cashInMode: ProfitCalculationMode = ProfitCalculationMode.DEFAULT,
    val cashOutMode: ProfitCalculationMode = ProfitCalculationMode.DEFAULT
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

    fun calculateCashIn() {
        val amount = AmountFormatter.parse(_uiState.value.cashInAmount)
        if (amount == null) {
            _uiState.update {
                it.copy(cashInResult = ProfitResult(0.0, "Enter a valid amount"))
            }
            return
        }
        viewModelScope.launch {
            val result = calculateVendorProfitUseCase.calculateCashIn(amount)
            _uiState.update { it.copy(cashInResult = result) }
        }
    }

    fun calculateCashOut() {
        val amount = AmountFormatter.parse(_uiState.value.cashOutAmount)
        if (amount == null) {
            _uiState.update {
                it.copy(cashOutResult = ProfitResult(0.0, "Enter a valid amount"))
            }
            return
        }
        viewModelScope.launch {
            val result = calculateVendorProfitUseCase.calculateCashOut(amount)
            _uiState.update { it.copy(cashOutResult = result) }
        }
    }
}
