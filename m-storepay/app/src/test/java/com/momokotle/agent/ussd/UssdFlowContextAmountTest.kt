package com.momokotle.agent.ussd

import com.momokotle.agent.utils.AmountFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class UssdFlowContextAmountTest {

    @Test
    fun resolve_amount_preservesDecimalPoint() {
        assertEquals(
            "10.50",
            UssdFlowContext(amount = "10.50").resolve("{amount}")
        )
        assertEquals(
            "25.5",
            UssdFlowContext(amount = "25.5").resolve("{amount}")
        )
        assertEquals(
            "100.75",
            UssdFlowContext(amount = "100.75").resolve("{amount}")
        )
        assertEquals(
            "10",
            UssdFlowContext(amount = "10").resolve("{amount}")
        )
    }

    @Test
    fun resolve_amount_doesNotStripDecimalLikeDigitOnlyFilter() {
        // Regression: filter { it.isDigit() } turned "10.50" into "1050".
        val resolved = UssdFlowContext(amount = "10.50").resolve("{amount}")
        assertEquals("10.50", resolved)
        assertEquals(false, resolved == "1050")
    }

    @Test
    fun resolve_phone_stillDigitNormalized() {
        assertEquals(
            "0241234567",
            UssdFlowContext(phoneNumber = "+233 24 123 4567").resolve("{phone}")
        )
    }

    @Test
    fun resolve_embeddedAmountInDirectUssdCode() {
        val context = UssdFlowContext(
            phoneNumber = "0241234567",
            amount = AmountFormatter.formatForUssd(10.50)
        )
        assertEquals(
            "*170*0241234567*10.5#",
            context.resolve("*170*{phone}*{amount}#")
        )
    }

    @Test
    fun dashboardStyle_formattedAmount_survivesResolve() {
        val launchAmount = AmountFormatter.formatForUssd(100.75)
        assertEquals("100.75", launchAmount)
        assertEquals(
            "100.75",
            UssdFlowContext(amount = launchAmount).resolve("{amount}")
        )
    }
}
