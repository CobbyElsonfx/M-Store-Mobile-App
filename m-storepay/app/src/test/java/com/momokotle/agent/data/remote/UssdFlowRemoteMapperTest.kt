package com.momokotle.agent.data.remote

import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.UssdActionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UssdFlowRemoteMapperTest {

    @Test
    fun map_preservesEveryAdminFlowInOrder() {
        val expected = mapOf(
            "MTN_CASHIN" to listOf("3", "1", "{phone}", "{phone_confirm}", "{amount}", null),
            "MTN_CASHOUT" to listOf("2", "1", "{phone}", "{phone_confirm}", "{amount}", null),
            "AGENT_MERCHANT" to listOf("1", "1", "{phone}", "{phone_confirm}", "{amount}", null),
            "MTN_AIRTIME" to listOf("5", "1", "5", "{amount}", "{phone}", "{phone_confirm}", null),
            "BALANCE" to listOf("7", "1", null),
            "COMMISSION" to listOf("7", "2", "1", null),
            "EVD_JUST4U" to listOf("8", null)
        )

        AdminUssdFlowFixtures.all().forEach { dto ->
            val flow = UssdFlowRemoteMapper.map(dto)!!
            assertEquals(TransactionType.valueOf(dto.flowKey), flow.transactionType)
            assertEquals(dto.ussdCode, flow.ussdCode)
            assertEquals(dto.flowKey, expected[dto.flowKey]!!, flow.steps.map { it.inputValue })
            if (dto.steps.last().type == "stop") {
                assertEquals(UssdActionType.STOP_FOR_USER, flow.steps.last().actionType)
            }
        }
    }

    @Test
    fun map_keepsAdminCustomizedCashOutSteps() {
        val dto = AdminUssdFlowFixtures.cashOut().copy(
            steps = listOf(
                ApiUssdStepDto("menu", "9"),
                ApiUssdStepDto("menu", "3"),
                ApiUssdStepDto("phone"),
                ApiUssdStepDto("phone_confirm"),
                ApiUssdStepDto("amount"),
                ApiUssdStepDto("reference", "Agent"),
                ApiUssdStepDto("stop")
            )
        )

        val flow = UssdFlowRemoteMapper.map(dto)!!
        assertEquals(
            listOf("9", "3", "{phone}", "{phone_confirm}", "{amount}", "Agent", null),
            flow.steps.map { it.inputValue }
        )
    }

    @Test
    fun map_doesNotDropUnknownAdminStep() {
        val dto = AdminUssdFlowFixtures.balance().copy(
            steps = listOf(
                ApiUssdStepDto("menu", "6"),
                ApiUssdStepDto("extra", "99"),
                ApiUssdStepDto("stop")
            )
        )

        val flow = UssdFlowRemoteMapper.map(dto)!!
        assertEquals(listOf("6", "99", null), flow.steps.map { it.inputValue })
    }

    @Test
    fun requirements_comeFromAdminStepsNotButtonType() {
        val cashIn = UssdFlowRemoteMapper.map(AdminUssdFlowFixtures.cashIn())!!
        val cashOut = UssdFlowRemoteMapper.map(AdminUssdFlowFixtures.cashOut())!!
        val balance = UssdFlowRemoteMapper.map(AdminUssdFlowFixtures.balance())!!
        val commission = UssdFlowRemoteMapper.map(AdminUssdFlowFixtures.commission())!!
        val balanceWithPhone = balance.copy(
            steps = listOf(
                com.momokotle.agent.ussd.UssdStep.inputPhoneNumber(),
                *balance.steps.toTypedArray()
            )
        )

        assertTrue(cashIn.requiresPhone && cashIn.requiresAmount)
        assertTrue(cashOut.requiresPhone && cashOut.requiresAmount)
        assertFalse(balance.requiresPhone)
        assertFalse(balance.requiresAmount)
        assertFalse(commission.requiresPhone)
        assertTrue(balanceWithPhone.requiresPhone)
        assertFalse(balanceWithPhone.requiresAmount)
    }
}
