package com.momokotle.agent.ussd

import com.momokotle.agent.models.TransactionType

object UssdFlows {
    /** Last-resort defaults only. Live buttons use the admin-stored API catalog. */

    const val MTN_AGENT_CODE = "*171#"

    private val MAIN_MENU = listOf("mobile money", "momo", "agent", "select", "welcome")
    private val SUB_MENU = listOf("select", "enter", "choose", "option")
    private val CONFIRM_HINTS = listOf("confirm", "re-enter", "again", "verify")

    val CashInFlow = UssdFlow(
        name = "CASH IN",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_CASHIN,
        steps = listOf(
            UssdStep.menuOption("3", MAIN_MENU + "cash"),
            UssdStep.menuOption("1", SUB_MENU + "cash"),
            UssdStep.inputPhoneNumber(listOf("number", "phone", "mobile", "recipient")),
            UssdStep.inputPhoneConfirm(CONFIRM_HINTS + listOf("number", "phone", "mobile")),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val CashOutFlow = UssdFlow(
        name = "CASH OUT",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_CASHOUT,
        steps = listOf(
            UssdStep.menuOption("2", MAIN_MENU + "cash"),
            UssdStep.menuOption("1", SUB_MENU + "cash"),
            UssdStep.inputPhoneNumber(listOf("number", "phone", "mobile", "recipient")),
            UssdStep.inputPhoneConfirm(CONFIRM_HINTS + listOf("number", "phone", "mobile")),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val PayToMerchantFlow = UssdFlow(
        name = "PAY TO MERCHANT",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.AGENT_MERCHANT,
        steps = listOf(
            UssdStep.menuOption("1", MAIN_MENU + "merchant"),
            UssdStep.menuOption("1", SUB_MENU + "merchant"),
            UssdStep.inputPhoneNumber(listOf("number", "phone", "mobile", "merchant", "recipient")),
            UssdStep.inputPhoneConfirm(CONFIRM_HINTS + listOf("number", "phone", "mobile")),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val TransferAirtimeFlow = UssdFlow(
        name = "TRANSFER AIRTIME",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_AIRTIME,
        steps = listOf(
            UssdStep.menuOption("5", MAIN_MENU + listOf("airtime", "data")),
            UssdStep.menuOption("1", SUB_MENU + listOf("airtime", "transfer", "buy")),
            UssdStep.menuOption("5", SUB_MENU + listOf("transfer", "number", "airtime")),
            UssdStep.inputAmount(),
            UssdStep.inputPhoneNumber(listOf("number", "phone", "mobile", "recipient")),
            UssdStep.inputPhoneConfirm(CONFIRM_HINTS + listOf("number", "phone", "mobile")),
            UssdStep.stopForUser()
        )
    )

    val BalanceFlow = UssdFlow(
        name = "BALANCE",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.BALANCE,
        steps = listOf(
            UssdStep.menuOption("7", MAIN_MENU + listOf("balance", "commission", "report", "my")),
            UssdStep.menuOption("1", SUB_MENU + listOf("balance", "wallet", "account")),
            UssdStep.stopForUser()
        )
    )

    val CommissionFlow = UssdFlow(
        name = "COMMISSION",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.COMMISSION,
        steps = listOf(
            UssdStep.menuOption("7", MAIN_MENU + listOf("balance", "commission", "report", "my")),
            UssdStep.menuOption("2", SUB_MENU + listOf("commission", "earnings", "report")),
            UssdStep.menuOption("1", SUB_MENU + listOf("commission", "view", "check", "today")),
            UssdStep.stopForUser()
        )
    )

    val EvdJust4uFlow = UssdFlow(
        name = "EVD/JUST4U",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.EVD_JUST4U,
        steps = listOf(
            UssdStep.menuOption("8", MAIN_MENU + listOf("evd", "just4u", "just 4 u")),
            UssdStep.stopForUser()
        )
    )

    fun forTransactionType(type: TransactionType): UssdFlow = when (type) {
        TransactionType.MTN_CASHIN -> CashInFlow
        TransactionType.MTN_CASHOUT -> CashOutFlow
        TransactionType.AGENT_MERCHANT -> PayToMerchantFlow
        TransactionType.MTN_AIRTIME -> TransferAirtimeFlow
        TransactionType.BALANCE -> BalanceFlow
        TransactionType.COMMISSION -> CommissionFlow
        TransactionType.EVD_JUST4U -> EvdJust4uFlow
        TransactionType.CUSTOM -> throw IllegalArgumentException("Use CustomFlowMapper for custom flows")
    }
}
