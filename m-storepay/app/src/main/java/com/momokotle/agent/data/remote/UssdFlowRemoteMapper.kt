package com.momokotle.agent.data.remote

import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.ussd.UssdStep

object UssdFlowRemoteMapper {

    private val MAIN_MENU_HINTS = listOf(
        "mobile money",
        "momo",
        "agent",
        "select",
        "welcome",
        "menu",
        "cash",
        "merchant",
        "airtime",
        "balance",
        "commission",
        "evd",
        "just4u"
    )

    private val SUB_MENU_HINTS = listOf("select", "enter", "choose", "option", "cash", "merchant", "airtime", "transfer")

    private val CONFIRM_HINTS = listOf("confirm", "re-enter", "again", "verify", "number", "phone", "mobile")

    fun map(dto: ApiUssdFlowDto): UssdFlow? {
        val transactionType = runCatching { TransactionType.valueOf(dto.flowKey) }.getOrNull()
            ?: return null
        if (dto.flowKey == TransactionType.CUSTOM.name) return null

        val steps = dto.steps.mapNotNull { mapStep(it) }
        if (steps.isEmpty()) return null

        return UssdFlow(
            name = dto.name,
            ussdCode = dto.ussdCode,
            transactionType = transactionType,
            steps = steps
        )
    }

    private fun mapStep(dto: ApiUssdStepDto): UssdStep? {
        return when (dto.type.lowercase()) {
            "menu" -> {
                val value = dto.value?.trim().orEmpty()
                if (value.isEmpty()) return null
                UssdStep.menuOption(value, dto.hints?.takeIf { it.isNotEmpty() } ?: MAIN_MENU_HINTS)
            }
            "phone" -> UssdStep.inputPhoneNumber(
                dto.hints?.takeIf { it.isNotEmpty() }
                    ?: listOf("number", "phone number", "mobile number", "recipient")
            )
            "phone_confirm", "confirm_phone" -> UssdStep.inputPhoneConfirm(
                dto.hints?.takeIf { it.isNotEmpty() } ?: CONFIRM_HINTS
            )
            "amount" -> UssdStep.inputAmount(
                dto.hints?.takeIf { it.isNotEmpty() }
                    ?: listOf("amount", "enter amount", "ghs", "cedi")
            )
            "reference" -> UssdStep(
                actionType = UssdActionType.INPUT,
                inputValue = dto.value?.takeIf { it.isNotBlank() } ?: "Pay",
                screenHints = dto.hints?.takeIf { it.isNotEmpty() }
                    ?: listOf("reference", "reason", "enter")
            )
            "stop", "stop_for_user" -> UssdStep.stopForUser()
            else -> {
                val value = dto.value?.trim().orEmpty().ifEmpty { dto.type.trim() }
                if (value.isEmpty()) null
                else UssdStep.menuOption(value, dto.hints?.takeIf { it.isNotEmpty() } ?: MAIN_MENU_HINTS)
            }
        }
    }
}
