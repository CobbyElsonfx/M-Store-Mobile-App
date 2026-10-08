package com.momokotle.agent.ussd

import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.CustomMenuType
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.utils.AmountFormatter
import com.momokotle.agent.utils.PhoneNumberFormatter

object CustomFlowExecutor {

    fun menuType(flow: CustomFlow): CustomMenuType {
        val hasSteps = flow.normalizedSteps().isNotEmpty()
        return if (hasSteps) CustomMenuType.STANDARD else CustomMenuType.DIRECT
    }

    fun requiresPhone(flow: CustomFlow): Boolean {
        val steps = flow.normalizedSteps()
        if (steps.any { isPhoneToken(it) }) return true
        if (menuType(flow) == CustomMenuType.DIRECT &&
            flow.ussdCode.contains("{phone}", ignoreCase = true)
        ) {
            return true
        }
        if (flow.useDynamicPhone && steps.any { it.equals("phonenumber", ignoreCase = true) }) {
            return true
        }
        return false
    }

    fun requiresAmount(flow: CustomFlow): Boolean {
        val steps = flow.normalizedSteps()
        if (steps.any { isAmountToken(it) }) return true
        if (menuType(flow) == CustomMenuType.DIRECT &&
            flow.ussdCode.contains("{amount}", ignoreCase = true)
        ) {
            return true
        }
        if (flow.useDynamicAmount && steps.any { it.equals("amt", ignoreCase = true) }) {
            return true
        }
        return false
    }

    fun canRunImmediately(flow: CustomFlow): Boolean =
        !requiresPhone(flow) && !requiresAmount(flow)

    /**
     * Validates home-screen phone/amount inputs for this flow.
     * @return error message, or null if valid.
     */
    fun validateForRun(
        flow: CustomFlow,
        rawPhone: String,
        rawAmount: String
    ): String? {
        val needsPhone = requiresPhone(flow)
        val needsAmount = requiresAmount(flow)
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(rawPhone)
        val amount = AmountFormatter.parse(rawAmount)

        if (needsPhone && !PhoneNumberFormatter.isValidGhanaNumber(phone)) {
            return "Enter a valid phone number"
        }
        if (needsAmount && amount == null) {
            return "Enter a valid amount"
        }
        return null
    }

    fun buildUssdFlow(
        flow: CustomFlow,
        rawPhone: String,
        rawAmount: String
    ): UssdFlow {
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(rawPhone)
        val amountValue = AmountFormatter.parse(rawAmount)
        val amountStr = amountValue?.let { AmountFormatter.formatForUssd(it) }.orEmpty()

        return when (menuType(flow)) {
            CustomMenuType.DIRECT -> {
                val context = UssdFlowContext(
                    phoneNumber = phone,
                    amount = amountStr
                )
                val resolvedCode = context.resolve(flow.ussdCode) ?: flow.ussdCode
                UssdFlow(
                    name = flow.name,
                    ussdCode = resolvedCode,
                    transactionType = TransactionType.CUSTOM,
                    steps = listOf(UssdStep.stopForUser())
                )
            }
            CustomMenuType.STANDARD -> CustomFlowMapper.toUssdFlow(flow)
        }
    }

    fun phoneForLaunch(flow: CustomFlow, rawPhone: String): String {
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(rawPhone)
        return if (requiresPhone(flow)) phone else ""
    }

    fun amountForLaunch(flow: CustomFlow, rawAmount: String): String {
        val amount = AmountFormatter.parse(rawAmount) ?: return ""
        return if (requiresAmount(flow)) AmountFormatter.formatForUssd(amount) else ""
    }

    fun amountForTransaction(flow: CustomFlow, rawAmount: String): Double =
        if (requiresAmount(flow)) AmountFormatter.parse(rawAmount) ?: 0.0 else 0.0

    fun phoneForTransaction(flow: CustomFlow, rawPhone: String): String? {
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(rawPhone)
        return phone.takeIf { requiresPhone(flow) }
    }

    /** Short label for dashboard grid (max ~2 lines). */
    fun gridLabel(name: String): String {
        val trimmed = name.trim()
        if (trimmed.length <= 14) return trimmed.uppercase()
        val words = trimmed.split(" ")
        if (words.size >= 2) {
            val mid = (words.size + 1) / 2
            return (words.take(mid).joinToString(" ") + "\n" + words.drop(mid).joinToString(" "))
                .uppercase()
        }
        return trimmed.uppercase()
    }

    private fun isPhoneToken(token: String): Boolean {
        val n = token.trim().lowercase()
        return n == "phone" || n == "{phone}" || n == "phonenumber"
    }

    private fun isAmountToken(token: String): Boolean {
        val n = token.trim().lowercase()
        return n == "amount" || n == "{amount}" || n == "amt"
    }
}
