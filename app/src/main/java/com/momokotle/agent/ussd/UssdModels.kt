package com.momokotle.agent.ussd

import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.UssdActionType

data class UssdStep(
    val expectedText: String? = null,
    val actionType: UssdActionType,
    val inputValue: String? = null,
    val nextCondition: String? = null,
    val timeoutMs: Long = 15_000L,
    val retryCount: Int = 3,
    val caseInsensitive: Boolean = true,
    val partialMatch: Boolean = true
) {
    companion object {
        fun menuOption(option: String) = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = option
        )

        fun inputPhoneNumber() = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = "{phone}"
        )

        fun inputAmount() = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = "{amount}"
        )

        fun stopForUser() = UssdStep(
            actionType = UssdActionType.STOP_FOR_USER
        )
    }
}

data class UssdFlow(
    val name: String,
    val ussdCode: String,
    val transactionType: TransactionType,
    val steps: List<UssdStep>,
    val stopBeforePin: Boolean = true
) {
    val triggerCode: String get() = ussdCode
}

data class UssdFlowContext(
    val phoneNumber: String? = null,
    val amount: String? = null,
    val recipient: String? = null,
    val extra: Map<String, String> = emptyMap()
) {
    fun resolve(template: String?): String? {
        if (template == null) return null
        return template
            .replace("{phone}", phoneNumber.orEmpty())
            .replace("{amount}", amount.orEmpty())
            .replace("{recipient}", recipient.orEmpty())
            .replace("{phoneNumber}", phoneNumber.orEmpty())
    }
}
