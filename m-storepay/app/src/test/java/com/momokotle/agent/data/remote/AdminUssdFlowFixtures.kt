package com.momokotle.agent.data.remote

/**
 * Mirrors [App\Support\UssdFlowDefaults] so Android tests prove every dashboard
 * button uses the same admin-stored step lists the API returns.
 */
object AdminUssdFlowFixtures {

    fun all(): List<ApiUssdFlowDto> = listOf(
        cashIn(),
        cashOut(),
        merchant(),
        airtime(),
        balance(),
        commission(),
        evd()
    )

    fun cashIn() = ApiUssdFlowDto(
        flowKey = "MTN_CASHIN",
        name = "CASH IN",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "3"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("phone"),
            ApiUssdStepDto("phone_confirm"),
            ApiUssdStepDto("amount"),
            ApiUssdStepDto("stop")
        )
    )

    fun cashOut() = ApiUssdFlowDto(
        flowKey = "MTN_CASHOUT",
        name = "CASH OUT",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "2"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("phone"),
            ApiUssdStepDto("phone_confirm"),
            ApiUssdStepDto("amount"),
            ApiUssdStepDto("stop")
        )
    )

    fun merchant() = ApiUssdFlowDto(
        flowKey = "AGENT_MERCHANT",
        name = "PAY TO MERCHANT",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("phone"),
            ApiUssdStepDto("phone_confirm"),
            ApiUssdStepDto("amount"),
            ApiUssdStepDto("stop")
        )
    )

    fun airtime() = ApiUssdFlowDto(
        flowKey = "MTN_AIRTIME",
        name = "TRANSFER AIRTIME",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "5"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("menu", "5"),
            ApiUssdStepDto("amount"),
            ApiUssdStepDto("phone"),
            ApiUssdStepDto("phone_confirm"),
            ApiUssdStepDto("stop")
        )
    )

    fun balance() = ApiUssdFlowDto(
        flowKey = "BALANCE",
        name = "BALANCE",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "7"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("stop")
        )
    )

    fun commission() = ApiUssdFlowDto(
        flowKey = "COMMISSION",
        name = "COMMISSION",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "7"),
            ApiUssdStepDto("menu", "2"),
            ApiUssdStepDto("menu", "1"),
            ApiUssdStepDto("stop")
        )
    )

    fun evd() = ApiUssdFlowDto(
        flowKey = "EVD_JUST4U",
        name = "EVD/JUST4U",
        ussdCode = "*171#",
        steps = listOf(
            ApiUssdStepDto("menu", "8"),
            ApiUssdStepDto("stop")
        )
    )
}
