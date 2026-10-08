package com.momokotle.agent.domain.engine

import com.momokotle.agent.models.AppSettings
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.models.ProfitRuleType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AgentCommissionEngineTest {

    private val engine = AgentCommissionEngine()

    private fun settings(
        cashInMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
        cashOutMode: ProfitCalculationMode = ProfitCalculationMode.PERCENTAGE,
        cashInPercent: Double = 0.01,
        cashOutPercent: Double = 0.015,
        cashInFixed: Double = 2.0,
        cashOutFixed: Double = 3.0
    ) = AppSettings(
        cashInCalculationMode = cashInMode,
        cashOutCalculationMode = cashOutMode,
        defaultCashInPercentage = cashInPercent,
        defaultCashOutPercentage = cashOutPercent,
        defaultCashInFixedCommission = cashInFixed,
        defaultCashOutFixedCommission = cashOutFixed
    )

    @Test
    fun percentageMode_calculatesCashInCommission() {
        val result = engine.calculateCashIn(
            amount = 100.0,
            settings = settings(cashInPercent = 0.01),
            tierRules = emptyList()
        )

        assertEquals(1.0, result.commission, 0.001)
        assertNotNull(result.ruleUsed)
    }

    @Test
    fun fixedMode_usesConfiguredFlatCommission() {
        val result = engine.calculateCashOut(
            amount = 500.0,
            settings = settings(
                cashOutMode = ProfitCalculationMode.FIXED,
                cashOutFixed = 3.5
            ),
            tierRules = emptyList()
        )

        assertEquals(3.5, result.commission, 0.001)
    }

    @Test
    fun tieredMode_appliesMatchingRange() {
        val tiers = listOf(
            ProfitRule(
                ruleType = ProfitRuleType.CASH_IN,
                minAmount = 1.0,
                maxAmount = 100.0,
                profitPercentage = 0.01
            ),
            ProfitRule(
                ruleType = ProfitRuleType.CASH_IN,
                minAmount = 101.0,
                maxAmount = 500.0,
                useFixedCommission = true,
                fixedCommission = 5.0
            )
        )

        val low = engine.calculateCashIn(50.0, settings(cashInMode = ProfitCalculationMode.TIERED), tiers)
        val high = engine.calculateCashIn(200.0, settings(cashInMode = ProfitCalculationMode.TIERED), tiers)

        assertEquals(0.5, low.commission, 0.001)
        assertEquals(5.0, high.commission, 0.001)
    }

    @Test
    fun tieredMode_noMatch_returnsZeroCommission() {
        val result = engine.calculateCashIn(
            amount = 1000.0,
            settings = settings(cashInMode = ProfitCalculationMode.TIERED),
            tierRules = listOf(
                ProfitRule(
                    ruleType = ProfitRuleType.CASH_IN,
                    minAmount = 1.0,
                    maxAmount = 100.0,
                    profitPercentage = 0.01
                )
            )
        )

        assertEquals(0.0, result.commission, 0.001)
        assertNotNull(result.ruleUsed)
    }

    @Test
    fun zeroOrNegativeAmount_returnsNoCommission() {
        val result = engine.calculateCashIn(
            amount = 0.0,
            settings = settings(),
            tierRules = emptyList()
        )

        assertEquals(0.0, result.commission, 0.001)
        assertNull(result.ruleUsed)
    }
}
