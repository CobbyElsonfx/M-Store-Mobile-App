package com.momokotle.agent.models

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val profit: Double = 0.0,
    val phoneNumber: String?,
    val flowName: String? = null,
    val status: TransactionStatus = TransactionStatus.ATTEMPTED,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

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
    val amount: Double,
    /** Decimal rate: 0.01 = 1%, 0.015 = 1.5% */
    val profitPercentage: Double
) {
    fun profitForAmount(amount: Double): Double =
        com.momokotle.agent.utils.PercentFormatter.calculateProfit(amount, profitPercentage)
}

data class ProfitResult(
    val profit: Double,
    val message: String? = null
)

data class CustomFlow(
    val id: Long = 0,
    val name: String,
    val ussdCode: String,
    val step1: String = "",
    val step2: String = "",
    val step3: String = "",
    val useDynamicPhone: Boolean = false,
    val useDynamicAmount: Boolean = false
)

data class AppSettings(
    val automationSpeed: AutomationSpeed = AutomationSpeed.NORMAL,
    val defaultSendConfirm: Boolean = true,
    val cashInCalculationMode: ProfitCalculationMode = ProfitCalculationMode.DEFAULT,
    val cashOutCalculationMode: ProfitCalculationMode = ProfitCalculationMode.DEFAULT,
    val defaultCashInPercentage: Double = ProfitDefaults.INITIAL_DECIMAL_RATE,
    val defaultCashOutPercentage: Double = ProfitDefaults.INITIAL_DECIMAL_RATE,
    val darkTheme: Boolean? = null,
    val transactionVerificationEnabled: Boolean = false,
    val vibrateOnTouch: Boolean = false,
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
