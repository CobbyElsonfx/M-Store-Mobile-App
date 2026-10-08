package com.momokotle.agent.ussd

import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.UssdActionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CustomFlowMapperTest {

    @Test
    fun toUssdFlow_mapsUpToTenSteps_plusStop() {
        val steps = (1..10).map { it.toString() }
        val flow = CustomFlow(
            name = "Deep menu",
            ussdCode = "*170#",
            steps = steps
        )

        val ussd = CustomFlowMapper.toUssdFlow(flow)

        assertEquals(11, ussd.steps.size)
        assertEquals(steps, ussd.steps.dropLast(1).map { it.inputValue })
        assertEquals(UssdActionType.STOP_FOR_USER, ussd.steps.last().actionType)
    }

    @Test
    fun normalizedSteps_capsAtMaxTen() {
        val flow = CustomFlow(
            name = "Overflow",
            ussdCode = "*170#",
            steps = (1..15).map { it.toString() }
        )

        assertEquals(10, flow.normalizedSteps().size)
        assertEquals((1..10).map { it.toString() }, flow.normalizedSteps())
    }

    @Test
    fun toUssdFlow_skipsBlankSteps() {
        val flow = CustomFlow(
            name = "Sparse",
            ussdCode = "*170#",
            steps = listOf("3", "", "phone", "  ", "amount")
        )

        val ussd = CustomFlowMapper.toUssdFlow(flow)
        assertEquals(listOf("3", "{phone}", "{amount}", null), ussd.steps.map { it.inputValue })
    }

    @Test
    fun executor_detectsPhoneAndAmountAcrossAllSteps() {
        val flow = CustomFlow(
            name = "Cash",
            ussdCode = "*170#",
            steps = listOf("3", "1", "2", "4", "5", "phone", "amount")
        )

        assertTrue(CustomFlowExecutor.requiresPhone(flow))
        assertTrue(CustomFlowExecutor.requiresAmount(flow))
        assertEquals(com.momokotle.agent.models.CustomMenuType.STANDARD, CustomFlowExecutor.menuType(flow))
    }
}
