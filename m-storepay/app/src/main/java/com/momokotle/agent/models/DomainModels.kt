package com.momokotle.agent.models

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    /** Agent earnings — kept in sync with [commission] for backward compatibility. */
    val profit: Double = 0.0,
    val commission: Double = 0.0,
    val commissionRuleUsed: String? = null,
    val phoneNumber: String?,
    val flowName: String? = null,
    val status: TransactionStatus = TransactionStatus.ATTEMPTED,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    val earnsCommission: Boolean
        get() = status == TransactionStatus.SUCCESSFUL && commission > 0
}

data class SavedContact(
    val id: Long = 0,
    val contactName: String,
    val phoneNumber: String,
    val network: ContactNetwork = ContactNetwork.UNKNOWN,
    val isPorted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class ProfitRule(
    val id: Long = 0,
    val ruleType: ProfitRuleType,
    val minAmount: Double,
    val maxAmount: Double,
    val fixedCommission: Double = 0.0,
    /** Decimal rate: 0.01 = 1%, 0.015 = 1.5% */
    val profitPercentage: Double = 0.0,
    val useFixedCommission: Boolean = false
) {
    fun contains(amount: Double): Boolean {
        val normalized = com.momokotle.agent.utils.ProfitAmountMatcher.normalize(amount)
        return normalized >= minAmount && normalized <= maxAmount
    }

    fun commissionForAmount(amount: Double): Double = when {
        useFixedCommission -> com.momokotle.agent.utils.ProfitAmountMatcher.normalize(fixedCommission)
        else -> com.momokotle.agent.utils.PercentFormatter.calculateProfit(amount, profitPercentage)
    }

    fun ruleLabel(): String = when {
        useFixedCommission -> "${formatAmount(minAmount)}–${formatAmount(maxAmount)} GHS → ${formatAmount(fixedCommission)} GHS fixed"
        minAmount == maxAmount -> "${formatAmount(minAmount)} GHS → ${com.momokotle.agent.utils.PercentFormatter.toLabel(profitPercentage)}"
        else -> "${formatAmount(minAmount)}–${formatAmount(maxAmount)} GHS → ${com.momokotle.agent.utils.PercentFormatter.toLabel(profitPercentage)}"
    }

    private fun formatAmount(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
}

data class AgentCommissionResult(
    val commission: Double,
    val ruleUsed: String?
) {
    val profit: Double get() = commission
}

data class CommissionSummary(
    /** Successful Cash-In transaction volume (GHS) for the period. */
    val cashInTotal: Double = 0.0,
    /** Successful Cash-Out transaction volume (GHS) for the period. */
    val cashOutTotal: Double = 0.0,
    val cashInCommission: Double = 0.0,
    val cashOutCommission: Double = 0.0,
    val totalCommission: Double = 0.0
) {
    val totalProfit: Double get() = totalCommission
    val totalVolume: Double get() = cashInTotal + cashOutTotal
}

data class ProfitResult(
    val profit: Double,
    val message: String? = null
)

data class CustomFlow(
    val id: Long = 0,
    val name: String,
    val ussdCode: String,
    /** Ordered USSD menu inputs; blank entries are ignored. Cap: [MAX_STEPS]. */
    val steps: List<String> = emptyList(),
    val useDynamicPhone: Boolean = false,
    val useDynamicAmount: Boolean = false
) {
    fun normalizedSteps(): List<String> =
        steps.map { it.trim() }.filter { it.isNotEmpty() }.take(MAX_STEPS)

    companion object {
        const val MAX_STEPS = 10
    }
}

data class AppSettings(
    val automationSpeed: AutomationSpeed = AutomationSpeed.NORMAL,
    val defaultSendConfirm: Boolean = true,
    val cashInCalculationMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
    val cashOutCalculationMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
    val defaultCashInPercentage: Double = ProfitDefaults.INITIAL_DECIMAL_RATE,
    val defaultCashOutPercentage: Double = ProfitDefaults.INITIAL_DECIMAL_RATE,
    val defaultCashInFixedCommission: Double = ProfitDefaults.INITIAL_FIXED_COMMISSION,
    val defaultCashOutFixedCommission: Double = ProfitDefaults.INITIAL_FIXED_COMMISSION,
    val darkTheme: Boolean? = null,
    val transactionVerificationEnabled: Boolean = false,
    val vibrateOnTouch: Boolean = false,
    val duplicateTransactionWarningWindow: DuplicateTransactionWarningWindow =
        DuplicateTransactionWarningWindow.FIVE_MINUTES,
    val simMtnMomo: SimSlot = SimSlot.NONE,
    val simTelecelCash: SimSlot = SimSlot.NONE,
    val simAirtelTigoCash: SimSlot = SimSlot.NONE,
    val simMtnAirtimeTransfer: SimSlot = SimSlot.NONE
) {
    fun simSlotFor(purpose: SimPurpose): SimSlot = when (purpose) {
        SimPurpose.MTN_MOBILE_MONEY -> simMtnMomo
        SimPurpose.TELECEL_CASH -> simTelecelCash
        SimPurpose.AIRTELTIGO_CASH -> simAirtelTigoCash
        SimPurpose.MTN_AIRTIME_TRANSFER -> simMtnAirtimeTransfer
    }
}
