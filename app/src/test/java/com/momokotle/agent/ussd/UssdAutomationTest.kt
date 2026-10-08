package com.momokotle.agent.ussd

import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.UssdSecurityHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UssdMenuLineMatcherTest {

    @Test
    fun lineMatchesOption_matchesNumberedMenuLines() {
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1. Transfer Money", "1"))
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1) MoMo User", "1"))
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1 Transfer", "1"))
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1", "1"))
    }

    @Test
    fun lineMatchesOption_rejectsOtherOptions() {
        assertFalse(UssdMenuLineMatcher.lineMatchesOption("2. Airtime", "1"))
        assertFalse(UssdMenuLineMatcher.lineMatchesOption("11. Something", "1"))
    }

    @Test
    fun findMatchingLine_returnsFirstMatchingLine() {
        val screen = """
            MTN Mobile Money
            1. Transfer Money
            2. Airtime
        """.trimIndent()

        assertEquals("1. Transfer Money", UssdMenuLineMatcher.findMatchingLine(screen, "1"))
    }
}

class UssdScreenValidatorTest {

    @Test
    fun isScreenReady_matchesConfiguredHints() {
        val step = UssdStep.menuOption("1", listOf("transfer", "mobile money"))
        assertTrue(
            UssdScreenValidator.isScreenReady(
                "Welcome to MTN Mobile Money\n1. Transfer Money",
                step
            )
        )
    }

    @Test
    fun isScreenReady_rejectsBlankScreen() {
        val step = UssdStep.inputAmount()
        assertFalse(UssdScreenValidator.isScreenReady("", step))
    }

    @Test
    fun isMenuOptionStep_detectsLiteralMenuValues() {
        assertTrue(UssdScreenValidator.isMenuOptionStep(UssdStep.menuOption("1")))
        assertFalse(UssdScreenValidator.isMenuOptionStep(UssdStep.inputPhoneNumber()))
    }
}

class UssdAutomationEngineTest {

    private val engine = UssdAutomationEngine(AutomationLogger())

    @Test
    fun advanceStep_movesThroughCashInFlowUntilPin() {
        engine.beginStart(
            flow = UssdFlows.CashInFlow,
            context = UssdFlowContext(phoneNumber = "0241234567", amount = "10")
        )
        engine.markRunning()

        repeat(UssdFlows.CashInFlow.steps.size) {
            engine.advanceStep()
        }

        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    @Test
    fun resolveInputValue_replacesPhoneAndAmountTemplates() {
        engine.beginStart(
            flow = UssdFlows.CashInFlow,
            context = UssdFlowContext(phoneNumber = "0241234567", amount = "25")
        )

        val phoneStep = UssdStep.inputPhoneNumber()
        val amountStep = UssdStep.inputAmount()

        assertEquals("0241234567", engine.resolveInputValue(phoneStep))
        assertEquals("25", engine.resolveInputValue(amountStep))
    }

    @Test
    fun currentStep_returnsNullWhenIdle() {
        assertNull(engine.currentStep())
    }
}

class UssdSecurityHelperTest {

    @Test
    fun containsSensitiveKeyword_detectsPinPrompts() {
        assertTrue(UssdSecurityHelper.containsSensitiveKeyword("Enter your PIN to confirm"))
        assertTrue(UssdSecurityHelper.containsSensitiveKeyword("Confirm with PIN"))
    }

    @Test
    fun containsSensitiveKeyword_ignoresUnrelatedText() {
        assertFalse(UssdSecurityHelper.containsSensitiveKeyword("1. Transfer Money"))
        assertFalse(UssdSecurityHelper.containsSensitiveKeyword("Enter recipient mobile number"))
    }
}
