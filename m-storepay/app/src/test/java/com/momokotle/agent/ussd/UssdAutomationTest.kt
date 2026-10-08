package com.momokotle.agent.ussd

import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.PhoneNumberFormatter
import com.momokotle.agent.utils.UssdSecurityHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UssdMenuLineMatcherTest {

    @Test
    fun lineMatchesOption_matchesNumberedMenuLines() {
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1. Transfer Money", "1"))
        assertTrue(UssdMenuLineMatcher.lineMatchesOption("1) MoMo User", "1"))
        assertFalse(UssdMenuLineMatcher.lineMatchesOption("11. Something", "1"))
    }
}

class UssdDialogSnapshotTest {

    @Test
    fun fingerprint_ignoresHintAndButtons() {
        val menu = UssdDialogSnapshot(
            text = "MTN Mobile Money\n1. Transfer Money\n2. Airtime\nInput required\nSend\nCancel",
            hasInputField = true
        )
        val sameMenu = UssdDialogSnapshot(
            text = "MTN Mobile Money\n1. Transfer Money\n2. Airtime",
            hasInputField = true
        )
        assertEquals(menu.fingerprint, sameMenu.fingerprint)
    }

    @Test
    fun fingerprint_changesWhenPromptChanges() {
        val menu = UssdDialogSnapshot("1. Transfer Money\n2. Airtime", true)
        val phone = UssdDialogSnapshot("Enter mobile number\nInput required", true)
        assertTrue(menu.fingerprint != phone.fingerprint)
    }
}

class UssdScreenValidatorTest {

    @Test
    fun containsFatalError_detectsInvalidOption() {
        assertTrue(UssdScreenValidator.containsFatalError("Invalid option. Try again"))
        assertFalse(UssdScreenValidator.containsFatalError("Enter mobile number"))
    }

    @Test
    fun isMenuOptionStep_onlyDigitMenus() {
        assertTrue(UssdScreenValidator.isMenuOptionStep(UssdStep.menuOption("1")))
        assertFalse(UssdScreenValidator.isMenuOptionStep(UssdStep.inputPhoneNumber()))
        assertFalse(
            UssdScreenValidator.isMenuOptionStep(
                UssdStep(actionType = UssdActionType.INPUT, inputValue = "Pay")
            )
        )
    }

    @Test
    fun requiresResolvedValue_forPhoneAndAmount() {
        assertTrue(UssdScreenValidator.requiresResolvedValue(UssdStep.inputPhoneNumber()))
        assertTrue(UssdScreenValidator.requiresResolvedValue(UssdStep.inputAmount()))
        assertFalse(UssdScreenValidator.requiresResolvedValue(UssdStep.menuOption("1")))
    }
}

class UssdAutomationEngineTest {

    private var now = 1_000L
    private lateinit var engine: UssdAutomationEngine

    @Before
    fun setUp() {
        now = 1_000L
        engine = UssdAutomationEngine(AutomationLogger()) { now }
    }

    private fun start(
        phone: String = "0241234567",
        amount: String = "10"
    ) {
        engine.beginStart(
            flow = UssdFlows.CashInFlow,
            context = UssdFlowContext(phoneNumber = phone, amount = amount)
        )
        engine.markRunning()
    }

    private fun dialog(text: String, hasField: Boolean = true) =
        UssdDialogSnapshot(text, hasField)

    @Test
    fun typedNumber_runsLinearlyThroughCashInUntilPin() {
        start(phone = "0241234567")
        driveCashInToPin()
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    @Test
    fun savedContactNumber_isNormalizedAndFilled() {
        start(phone = "+233 24 123 4567")
        assertEquals("0241234567", engine.resolveInputValue(UssdStep.inputPhoneNumber()))
        driveCashInToPin()
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    @Test
    fun sameDialogAfterSubmit_waitsInsteadOfDoubleTyping() {
        start()
        val menu = dialog("MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In\nInput required")
        val first = engine.evaluate(menu)
        assertTrue(first is AutomationDecision.Act)
        assertEquals("3", (first as AutomationDecision.Act).value)
        engine.onSubmitted(menu.fingerprint)

        val wait = engine.evaluate(menu)
        assertEquals(AutomationDecision.Wait, wait)
        assertEquals(AutomationStatus.RUNNING, engine.status.value)
    }

    @Test
    fun cashInFlow_includesPhoneConfirmBeforeAmount() {
        val types = UssdFlows.CashInFlow.steps.map { it.inputValue }
        assertEquals(listOf("3", "1", "{phone}", "{phone_confirm}", "{amount}", null), types)
        assertEquals(UssdFlows.MTN_AGENT_CODE, UssdFlows.CashInFlow.ussdCode)
    }

    @Test
    fun everyAdminStoredFlow_runsEachStepInOrder() {
        com.momokotle.agent.data.remote.AdminUssdFlowFixtures.all().forEach { dto ->
            val flow = com.momokotle.agent.data.remote.UssdFlowRemoteMapper.map(dto)!!
            engine = UssdAutomationEngine(AutomationLogger()) { now }
            engine.beginStart(
                flow = flow,
                context = UssdFlowContext(phoneNumber = "0241234567", amount = "10")
            )
            engine.markRunning()

            flow.steps.forEachIndexed { index, step ->
                if (index > 0) engine.noteNewWindow()
                val screen = dialog("Admin step $index ${step.inputValue ?: step.actionType}")
                val decision = engine.evaluate(screen)
                if (step.actionType == UssdActionType.STOP_FOR_USER) {
                    assertEquals(
                        "Expected PIN stop on ${dto.flowKey} step $index",
                        AutomationDecision.WaitForPin,
                        decision
                    )
                } else {
                    assertTrue(
                        "Expected Act on ${dto.flowKey} step $index (${step.inputValue})",
                        decision is AutomationDecision.Act
                    )
                    val act = decision as AutomationDecision.Act
                    assertEquals(step.inputValue, act.step.inputValue)
                    engine.onSubmitted(screen.fingerprint, act.value)
                }
            }
        }
    }

    @Test
    fun nextDialog_usesCurrentStepValueNotHintMatching() {
        start()
        engine.onSubmitted(dialog("1. Merchant\n2. Cash Out\n3. Cash In").fingerprint, "1")
        engine.onSubmitted(dialog("1. Customer wallet\n2. Other").fingerprint, "1")
        engine.noteNewWindow()

        val phonePrompt = dialog("Input required")
        val decision = engine.evaluate(phonePrompt)
        assertTrue(decision is AutomationDecision.Act)
        assertEquals("{phone}", (decision as AutomationDecision.Act).step.inputValue)
        assertEquals("0241234567", decision.value)
    }

    @Test
    fun confirmNumber_isNotSkippedWhenDialogLooksTheSame() {
        start()
        engine.onSubmitted(dialog("3. Cash In").fingerprint, "1")
        engine.onSubmitted(dialog("1. Customer wallet").fingerprint, "1")
        engine.noteNewWindow()

        val phone = dialog("Input required")
        engine.evaluate(phone)
        engine.onSubmitted(phone.fingerprint, "0241234567")

        val sameLookingConfirm = dialog("Input required")
        assertEquals(AutomationDecision.Wait, engine.evaluate(sameLookingConfirm))

        engine.noteNewWindow()
        val confirm = engine.evaluate(sameLookingConfirm)
        assertTrue(confirm is AutomationDecision.Act)
        assertEquals("{phone_confirm}", (confirm as AutomationDecision.Act).step.inputValue)
        assertEquals("0241234567", confirm.value)
        engine.onSubmitted(sameLookingConfirm.fingerprint, "0241234567")

        engine.noteNewWindow()
        val amount = engine.evaluate(dialog("Enter amount"))
        assertTrue(amount is AutomationDecision.Act)
        assertEquals("{amount}", (amount as AutomationDecision.Act).step.inputValue)
        assertEquals("10", amount.value)
    }

    @Test
    fun typedDigitsInSamePrompt_doNotSkipToNextStep() {
        start()
        val menu = dialog("1. Transfer Money\n2. MoMoPay")
        assertTrue(engine.evaluate(menu) is AutomationDecision.Act)
        engine.onSubmitted(menu.fingerprint, "1")

        // Accessibility dump often includes the typed digit — must not look like a new prompt.
        val sameMenuWithTypedDigit = dialog("1. Transfer Money\n2. MoMoPay\n1")
        assertEquals(AutomationDecision.Wait, engine.evaluate(sameMenuWithTypedDigit))
        assertEquals(0, engine.currentStepIndex.value)
    }

    @Test
    fun missingPhone_failsImmediately() {
        start(phone = "")
        val menu = dialog("3. Cash In")
        engine.onSubmitted(menu.fingerprint, "1")
        engine.onSubmitted(dialog("1. Customer wallet").fingerprint, "1")
        engine.noteNewWindow()

        val decision = engine.evaluate(dialog("Enter number"))
        assertTrue(decision is AutomationDecision.FinishFailure)
        engine.applyDecision(decision)
        assertEquals(AutomationStatus.FAILED, engine.status.value)
    }

    @Test
    fun stuckOnSameDialog_failsWhenStepTimesOut() {
        start()
        val menu = dialog("1. Merchant\n2. Cash Out\n3. Cash In")
        engine.evaluate(menu)
        engine.onSubmitted(menu.fingerprint, "1")

        now += 25_000L
        val decision = engine.evaluate(menu)
        assertTrue(decision is AutomationDecision.FinishFailure)
        engine.applyDecision(decision)
        assertTrue(engine.status.value.isTerminal)
        assertEquals(AutomationStatus.FAILED, engine.status.value)
    }

    @Test
    fun pinPrompt_stopsAutomationForManualEntry() {
        start()
        val decision = engine.evaluate(dialog("Enter your PIN to confirm"))
        assertEquals(AutomationDecision.WaitForPin, decision)
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    @Test
    fun pinWait_releasesSessionAfterTimeoutWithoutClaimingSuccess() {
        start()
        engine.evaluate(dialog("Enter your PIN to confirm"))
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)

        now += UssdAutomationEngine.PIN_WAIT_MS
        val decision = engine.evaluate(dialog("Enter your PIN to confirm"))
        assertEquals(AutomationDecision.SessionEnded, decision)
        engine.applyDecision(decision)
        assertEquals(AutomationStatus.SESSION_ENDED, engine.status.value)
        assertFalse(engine.status.value.isActive)
    }

    @Test
    fun pinWait_stillWaitsBeforeTimeout() {
        start()
        engine.evaluate(dialog("Enter your PIN to confirm"))
        now += UssdAutomationEngine.PIN_WAIT_MS - 1_000L
        val decision = engine.evaluate(dialog("Enter your PIN to confirm"))
        assertEquals(AutomationDecision.WaitForPin, decision)
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    @Test
    fun pinWait_succeedsWhenSuccessKeywordsAppear() {
        start()
        engine.evaluate(dialog("Enter your PIN to confirm"))
        val decision = engine.evaluate(dialog("Transaction successful"))
        engine.applyDecision(decision)
        assertEquals(AutomationStatus.SUCCESS, engine.status.value)
    }

    @Test
    fun dialogAbsence_afterSeenDialog_endsSessionWithoutSuccess() {
        start()
        engine.evaluate(dialog("MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In"))
        assertTrue(engine.status.value.isActive)

        now += 100L
        engine.evaluate(dialog("", hasField = false))
        now += UssdAutomationEngine.SESSION_ABSENCE_MS
        val decision = engine.evaluate(dialog("", hasField = false))
        assertEquals(AutomationDecision.SessionEnded, decision)
        engine.applyDecision(decision)
        assertEquals(AutomationStatus.SESSION_ENDED, engine.status.value)
        assertFalse(engine.status.value.isActive)
    }

    @Test
    fun newSession_ignoresStaleSessionId() {
        start()
        val firstSession = engine.currentSessionId
        assertTrue(engine.isCurrentSession(firstSession))

        engine.markSessionEnded()
        assertFalse(engine.isCurrentSession(firstSession))

        start()
        val secondSession = engine.currentSessionId
        assertTrue(secondSession > firstSession)
        assertFalse(engine.isCurrentSession(firstSession))
        assertTrue(engine.isCurrentSession(secondSession))
    }

    @Test
    fun sessionEnded_allowsImmediateRestart() {
        start()
        engine.evaluate(dialog("3. Cash In"))
        engine.evaluate(dialog("", hasField = false))
        now += UssdAutomationEngine.SESSION_ABSENCE_MS + 1
        engine.applyDecision(engine.evaluate(dialog("", hasField = false)))
        assertFalse(engine.isRunning)

        start()
        assertTrue(engine.isRunning)
        assertEquals(AutomationStatus.RUNNING, engine.status.value)
    }

    @Test
    fun fatalError_terminatesFailed() {
        start()
        val decision = engine.evaluate(dialog("Invalid option selected"))
        engine.applyDecision(decision)
        assertEquals(AutomationStatus.FAILED, engine.status.value)
    }

    @Test
    fun currentStep_returnsNullWhenIdle() {
        assertNull(engine.currentStep())
    }

    @Test
    fun advanceStep_reachesPinAfterAllCashInSteps() {
        start()
        repeat(UssdFlows.CashInFlow.steps.size) { engine.advanceStep() }
        assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
    }

    private fun driveCashInToPin() {
        val screens = listOf(
            dialog("MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In"),
            dialog("1. Customer wallet\n2. Other"),
            dialog("Enter mobile number"),
            dialog("Confirm mobile number"),
            dialog("Enter amount")
        )
        val expectedTokens = listOf("3", "1", "{phone}", "{phone_confirm}", "{amount}")
        screens.zip(expectedTokens).forEachIndexed { index, (screen, token) ->
            if (index > 0) engine.noteNewWindow()
            val decision = engine.evaluate(screen)
            assertTrue("Expected Act on ${screen.text}", decision is AutomationDecision.Act)
            assertEquals(token, (decision as AutomationDecision.Act).step.inputValue)
            engine.onSubmitted(screen.fingerprint, decision.value)
        }
        engine.noteNewWindow()
        val pin = engine.evaluate(dialog("Enter your PIN to confirm"))
        assertEquals(AutomationDecision.WaitForPin, pin)
    }
}

class PhoneNumberFormatterTest {

    @Test
    fun normalize_acceptsTypedLocalNumber() {
        assertEquals("0241234567", PhoneNumberFormatter.normalizeGhanaNumber("0241234567"))
        assertTrue(PhoneNumberFormatter.isValidGhanaNumber("0241234567"))
    }

    @Test
    fun normalize_acceptsSavedInternationalAndSpacedNumbers() {
        assertEquals("0241234567", PhoneNumberFormatter.normalizeGhanaNumber("+233 24 123 4567"))
        assertEquals("0241234567", PhoneNumberFormatter.normalizeGhanaNumber("233241234567"))
        assertTrue(PhoneNumberFormatter.isValidGhanaNumber("+233241234567"))
    }
}

class UssdSecurityHelperTest {

    @Test
    fun containsSensitiveKeyword_detectsPinPrompts() {
        assertTrue(UssdSecurityHelper.containsSensitiveKeyword("Enter your PIN to confirm"))
        assertFalse(UssdSecurityHelper.containsSensitiveKeyword("Enter recipient mobile number"))
    }
}
