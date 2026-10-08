package com.momokotle.agent.ussd

import com.momokotle.agent.data.remote.AdminUssdFlowFixtures
import com.momokotle.agent.data.remote.ApiUssdFlowDto
import com.momokotle.agent.data.remote.UssdFlowRemoteMapper
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.utils.AutomationLogger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * QA simulation: each dashboard button must follow the admin-stored step list.
 * Typed values are checked on every screen so confirm is never replaced by amount.
 */
class UssdFlowQaSimulationTest {

    private var now = 1_000L
    private lateinit var engine: UssdAutomationEngine

    @Before
    fun setUp() {
        now = 1_000L
        engine = UssdAutomationEngine(AutomationLogger()) { now }
    }

    @Test
    fun cashIn_followsAdminSteps_andConfirmsNumberBeforeAmount() {
        simulate(
            dto = AdminUssdFlowFixtures.cashIn(),
            screens = listOf(
                Screen("MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In", "3", "3"),
                Screen("1. Customer wallet\n2. Other", "1", "1"),
                Screen("Enter mobile number", "{phone}", PHONE),
                Screen("Confirm mobile number", "{phone_confirm}", PHONE),
                Screen("Enter amount", "{amount}", AMOUNT)
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun cashOut_followsAdminSteps_andConfirmsNumberBeforeAmount() {
        simulate(
            dto = AdminUssdFlowFixtures.cashOut(),
            screens = listOf(
                Screen("MTN Agent\n1. Merchant\n2. Cash Out", "2", "2"),
                Screen("1. Customer wallet\n2. Other", "1", "1"),
                Screen("Enter customer number", "{phone}", PHONE),
                Screen("Confirm customer number", "{phone_confirm}", PHONE),
                Screen("Enter amount", "{amount}", AMOUNT)
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun payToMerchant_followsAdminSteps_andConfirmsNumberBeforeAmount() {
        simulate(
            dto = AdminUssdFlowFixtures.merchant(),
            screens = listOf(
                Screen("MTN Agent\n1. Pay Merchant\n2. Cash Out", "1", "1"),
                Screen("1. Merchant till\n2. Other", "1", "1"),
                Screen("Enter merchant number", "{phone}", PHONE),
                Screen("Confirm merchant number", "{phone_confirm}", PHONE),
                Screen("Enter amount", "{amount}", AMOUNT)
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun transferAirtime_followsAdminSteps_amountBeforePhone() {
        simulate(
            dto = AdminUssdFlowFixtures.airtime(),
            screens = listOf(
                Screen("MTN Agent\n5. Airtime and Data", "5", "5"),
                Screen("1. Transfer Airtime\n2. Buy", "1", "1"),
                Screen("5. Transfer to number", "5", "5"),
                Screen("Enter amount", "{amount}", AMOUNT),
                Screen("Enter recipient number", "{phone}", PHONE),
                Screen("Confirm recipient number", "{phone_confirm}", PHONE)
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun balance_followsAdminSteps_withoutPhoneOrAmount() {
        val flow = mapped(AdminUssdFlowFixtures.balance())
        assertEquals(false, flow.requiresPhone)
        assertEquals(false, flow.requiresAmount)
        simulate(
            dto = AdminUssdFlowFixtures.balance(),
            screens = listOf(
                Screen("MTN Agent\n7. My Account", "7", "7"),
                Screen("1. Balance\n2. Commission", "1", "1")
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun commission_followsAdminSteps_withoutPhoneOrAmount() {
        simulate(
            dto = AdminUssdFlowFixtures.commission(),
            screens = listOf(
                Screen("MTN Agent\n7. My Account", "7", "7"),
                Screen("1. Balance\n2. Commission", "2", "2"),
                Screen("1. Check commission", "1", "1")
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun evdJust4u_followsAdminSteps_withoutPhoneOrAmount() {
        simulate(
            dto = AdminUssdFlowFixtures.evd(),
            screens = listOf(
                Screen("MTN Agent\n8. EVD/JUST4U", "8", "8")
            ),
            expectPinAfterLastSubmit = true
        )
    }

    @Test
    fun cashOut_doesNotTypeAmountOnConfirmScreen() {
        start(mapped(AdminUssdFlowFixtures.cashOut()))
        submit("2. Cash Out")
        submit("1. Customer wallet")
        submit("Enter customer number")
        engine.noteNewWindow()

        val confirm = engine.evaluate(dialog("Confirm customer number"))
        assertTrue(confirm is AutomationDecision.Act)
        val act = confirm as AutomationDecision.Act
        assertEquals("{phone_confirm}", act.step.inputValue)
        assertEquals(PHONE, act.value)
        assertTrue(act.value != AMOUNT)
    }

    @Test
    fun adminCustomCashOut_followsStoredStepsNotBuiltInDefaults() {
        val customized = AdminUssdFlowFixtures.cashOut().copy(
            steps = listOf(
                com.momokotle.agent.data.remote.ApiUssdStepDto("menu", "9"),
                com.momokotle.agent.data.remote.ApiUssdStepDto("phone"),
                com.momokotle.agent.data.remote.ApiUssdStepDto("amount"),
                com.momokotle.agent.data.remote.ApiUssdStepDto("stop")
            )
        )
        simulate(
            dto = customized,
            screens = listOf(
                Screen("Custom agent menu", "9", "9"),
                Screen("Enter number", "{phone}", PHONE),
                Screen("Enter amount", "{amount}", AMOUNT)
            ),
            expectPinAfterLastSubmit = true
        )
    }

    private fun simulate(
        dto: ApiUssdFlowDto,
        screens: List<Screen>,
        expectPinAfterLastSubmit: Boolean
    ) {
        val flow = mapped(dto)
        start(flow)
        screens.forEachIndexed { index, screen ->
            if (index > 0) engine.noteNewWindow()
            val decision = engine.evaluate(dialog(screen.text))
            assertTrue(
                "${dto.flowKey}: expected Act on '${screen.text}', got $decision",
                decision is AutomationDecision.Act
            )
            val act = decision as AutomationDecision.Act
            assertEquals("${dto.flowKey} token on '${screen.text}'", screen.token, act.step.inputValue)
            assertEquals("${dto.flowKey} typed value on '${screen.text}'", screen.typed, act.value)
            engine.onSubmitted(UssdDialogSnapshot(screen.text, true).fingerprint, act.value)
        }
        if (expectPinAfterLastSubmit) {
            engine.noteNewWindow()
            val pin = engine.evaluate(dialog("Enter your PIN to confirm"))
            assertEquals("${dto.flowKey} must stop for PIN", AutomationDecision.WaitForPin, pin)
            assertEquals(AutomationStatus.WAITING_FOR_PIN, engine.status.value)
        }
    }

    private fun start(flow: UssdFlow) {
        engine = UssdAutomationEngine(AutomationLogger()) { now }
        engine.beginStart(
            flow = flow,
            context = UssdFlowContext(phoneNumber = PHONE, amount = AMOUNT)
        )
        engine.markRunning()
    }

    private fun submit(text: String) {
        val snapshot = dialog(text)
        engine.noteNewWindow()
        engine.evaluate(snapshot)
        engine.onSubmitted(snapshot.fingerprint)
    }

    private fun mapped(dto: ApiUssdFlowDto): UssdFlow = UssdFlowRemoteMapper.map(dto)!!

    private fun dialog(text: String) = UssdDialogSnapshot(text, true)

    private data class Screen(val text: String, val token: String, val typed: String)

    companion object {
        private const val PHONE = "0241234567"
        private const val AMOUNT = "10"
    }
}
