package com.momokotle.agent.domain.engine

import com.momokotle.agent.models.AgentCommissionResult
import com.momokotle.agent.models.AppSettings
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.utils.PercentFormatter
import com.momokotle.agent.utils.ProfitAmountMatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentCommissionEngine @Inject constructor() {

    fun calculateCashIn(
        amount: Double,
        settings: AppSettings,
        tierRules: List<ProfitRule>
    ): AgentCommissionResult = calculate(
        amount = amount,
        ruleType = ProfitRuleType.CASH_IN,
        mode = settings.cashInCalculationMode,
        defaultPercent = settings.defaultCashInPercentage,
        defaultFixed = settings.defaultCashInFixedCommission,
        tierRules = tierRules
    )

    fun calculateCashOut(
        amount: Double,
        settings: AppSettings,
        tierRules: List<ProfitRule>
    ): AgentCommissionResult = calculate(
        amount = amount,
        ruleType = ProfitRuleType.CASH_OUT,
        mode = settings.cashOutCalculationMode,
        defaultPercent = settings.defaultCashOutPercentage,
        defaultFixed = settings.defaultCashOutFixedCommission,
        tierRules = tierRules
    )

    fun calculateForTransaction(
        type: TransactionType,
        amount: Double,
        settings: AppSettings,
        cashInRules: List<ProfitRule>,
        cashOutRules: List<ProfitRule>
    ): AgentCommissionResult = when (type) {
        TransactionType.MTN_CASHIN -> calculateCashIn(amount, settings, cashInRules)
        TransactionType.MTN_CASHOUT -> calculateCashOut(amount, settings, cashOutRules)
        else -> AgentCommissionResult(0.0, null)
    }

    private fun calculate(
        amount: Double,
        ruleType: ProfitRuleType,
        mode: ProfitCalculationMode,
        defaultPercent: Double,
        defaultFixed: Double,
        tierRules: List<ProfitRule>
    ): AgentCommissionResult {
        if (amount <= 0) return AgentCommissionResult(0.0, null)
        val normalized = ProfitAmountMatcher.normalize(amount)
        val prefix = ruleType.displayName

        return when (mode) {
            ProfitCalculationMode.PERCENTAGE -> {
                val commission = PercentFormatter.calculateProfit(normalized, defaultPercent)
                AgentCommissionResult(
                    commission = commission,
                    ruleUsed = "$prefix Percentage ${PercentFormatter.toLabel(defaultPercent)}"
                )
            }
            ProfitCalculationMode.FIXED -> {
                val commission = ProfitAmountMatcher.normalize(defaultFixed)
                AgentCommissionResult(
                    commission = commission,
                    ruleUsed = "$prefix Fixed ${formatGhs(commission)} GHS"
                )
            }
            ProfitCalculationMode.TIERED -> {
                val rule = tierRules.firstOrNull { it.contains(normalized) }
                    ?: return AgentCommissionResult(
                        commission = 0.0,
                        ruleUsed = "$prefix Tier — no matching range"
                    )
                val commission = rule.commissionForAmount(normalized)
                AgentCommissionResult(
                    commission = commission,
                    ruleUsed = "$prefix Tier ${rule.ruleLabel()}"
                )
            }
        }
    }

    private fun formatGhs(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
}
