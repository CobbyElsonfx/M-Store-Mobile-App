package com.momokotle.agent.ussd

import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.TransactionType

object CustomFlowMapper {

    fun toUssdFlow(custom: CustomFlow): UssdFlow {
        val rawSteps = custom.normalizedSteps()

        val steps = rawSteps.map { token -> tokenToStep(token, custom) } + UssdStep.stopForUser()

        return UssdFlow(
            name = custom.name,
            ussdCode = custom.ussdCode,
            transactionType = TransactionType.CUSTOM,
            steps = steps
        )
    }

    private fun tokenToStep(token: String, custom: CustomFlow): UssdStep {
        val normalized = token.lowercase()
        return when {
            normalized == "phone" || normalized == "{phone}" -> UssdStep.inputPhoneNumber()
            normalized == "phone_confirm" || normalized == "{phone_confirm}" ||
                normalized == "confirm_phone" -> UssdStep.inputPhoneConfirm()
            normalized == "amount" || normalized == "{amount}" -> UssdStep.inputAmount()
            custom.useDynamicPhone && normalized == "phonenumber" -> UssdStep.inputPhoneNumber()
            custom.useDynamicAmount && normalized == "amt" -> UssdStep.inputAmount()
            else -> UssdStep.menuOption(token, listOf("select", "enter", "mobile money", "momo", "menu"))
        }
    }
}
