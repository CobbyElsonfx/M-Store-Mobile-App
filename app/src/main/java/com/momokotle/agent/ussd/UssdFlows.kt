package com.momokotle.agent.ussd

import com.momokotle.agent.models.TransactionType

object UssdFlows {

    const val MTN_AGENT_CODE = "*171#"

    val CashInFlow = UssdFlow(
        name = "CASH IN",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_CASHIN,
        steps = listOf(
            UssdStep.menuOption("3"),
            UssdStep.menuOption("1"),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val CashOutFlow = UssdFlow(
        name = "CASH OUT",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_CASHOUT,
        steps = listOf(
            UssdStep.menuOption("2"),
            UssdStep.menuOption("1"),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val PayToMerchantFlow = UssdFlow(
        name = "PAY TO MERCHANT",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.AGENT_MERCHANT,
        steps = listOf(
            UssdStep.menuOption("1"),
            UssdStep.menuOption("1"),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val TransferAirtimeFlow = UssdFlow(
        name = "TRANSFER AIRTIME",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.MTN_AIRTIME,
        steps = listOf(
            UssdStep.menuOption("5"),
            UssdStep.menuOption("1"),
            UssdStep.menuOption("5"),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputPhoneNumber(),
            UssdStep.inputAmount(),
            UssdStep.stopForUser()
        )
    )

    val BalanceFlow = UssdFlow(
        name = "BALANCE",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.BALANCE,
        steps = listOf(
            UssdStep.menuOption("6"),
            UssdStep.stopForUser()
        )
    )

    val CommissionFlow = UssdFlow(
        name = "COMMISSION",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.COMMISSION,
        steps = listOf(
            UssdStep.menuOption("7"),
            UssdStep.stopForUser()
        )
    )

    val EvdJust4uFlow = UssdFlow(
        name = "EVD/JUST4U",
        ussdCode = MTN_AGENT_CODE,
        transactionType = TransactionType.EVD_JUST4U,
        steps = listOf(
            UssdStep.menuOption("8"),
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
