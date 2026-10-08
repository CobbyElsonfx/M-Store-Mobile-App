package com.momokotle.agent.ussd

import com.google.gson.annotations.SerializedName
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.utils.AmountFormatter
import com.momokotle.agent.utils.PhoneNumberFormatter

data class UssdStep(
    @SerializedName("expectedText") val expectedText: String? = null,
    @SerializedName("actionType") val actionType: UssdActionType,
    @SerializedName("inputValue") val inputValue: String? = null,
    @SerializedName("nextCondition") val nextCondition: String? = null,
    @SerializedName("timeoutMs") val timeoutMs: Long = 20_000L,
    @SerializedName("retryCount") val retryCount: Int = 8,
    @SerializedName("caseInsensitive") val caseInsensitive: Boolean = true,
    @SerializedName("partialMatch") val partialMatch: Boolean = true,
    @SerializedName("screenHints") val screenHints: List<String> = emptyList()
) {
    companion object {
        private val MAIN_MENU_HINTS = listOf(
            "mobile money",
            "momo",
            "agent",
            "select",
            "welcome",
            "menu"
        )

        fun menuOption(option: String, hints: List<String> = MAIN_MENU_HINTS) = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = option,
            screenHints = hints
        )

        fun inputPhoneNumber(hints: List<String> = listOf(
            "number",
            "phone number",
            "mobile number",
            "recipient"
        )) = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = "{phone}",
            screenHints = hints
        )

        fun inputPhoneConfirm(hints: List<String> = listOf(
            "confirm",
            "re-enter",
            "again",
            "verify"

        )) = UssdStep(            actionType = UssdActionType.INPUT,
            inputValue = "{phone_confirm}",
            screenHints = hints
        )

        fun inputAmount(hints: List<String> = listOf(
            "amount",
            "enter amount",
            "ghs",
            "cedi"
        )) = UssdStep(
            actionType = UssdActionType.INPUT,
            inputValue = "{amount}",
            screenHints = hints
        )

        fun stopForUser() = UssdStep(
            actionType = UssdActionType.STOP_FOR_USER
        )
    }
}

data class UssdFlow(
    @SerializedName("name") val name: String,
    @SerializedName("ussdCode") val ussdCode: String,
    @SerializedName("transactionType") val transactionType: TransactionType,
    @SerializedName("steps") val steps: List<UssdStep>,
    @SerializedName("stopBeforePin") val stopBeforePin: Boolean = true
) {
    val triggerCode: String get() = ussdCode

    val requiresPhone: Boolean
        get() = steps.any { it.inputValue in PHONE_TOKENS }

    val requiresAmount: Boolean
        get() = steps.any { it.inputValue == "{amount}" }

    companion object {
        val PHONE_TOKENS = setOf("{phone}", "{phone_confirm}", "{phoneNumber}")
    }
}

data class UssdFlowContext(
    val phoneNumber: String? = null,
    val amount: String? = null,
    val recipient: String? = null,
    val extra: Map<String, String> = emptyMap()
) {
    fun resolve(template: String?): String? {
        if (template == null) return null
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber.orEmpty())
        val ussdAmount = AmountFormatter.sanitizeForUssd(amount.orEmpty())
        return template
            .replace("{phone_confirm}", phone)
            .replace("{phone}", phone)
            .replace("{amount}", ussdAmount)
            .replace("{recipient}", PhoneNumberFormatter.normalizeGhanaNumber(recipient.orEmpty()).ifBlank { phone })
            .replace("{phoneNumber}", phone)
    }
}
