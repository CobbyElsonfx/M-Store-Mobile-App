package com.momokotle.agent.domain.usecase

import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class CommissionSummaryCalculatorTest {

    private val todayStart: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun tx(
        type: TransactionType,
        amount: Double,
        commission: Double = 0.0,
        status: TransactionStatus = TransactionStatus.SUCCESSFUL,
        timestamp: Long = todayStart + 3_600_000L
    ) = Transaction(
        type = type,
        amount = amount,
        commission = commission,
        profit = commission,
        phoneNumber = "0244000000",
        status = status,
        timestamp = timestamp
    )

    @Test
    fun today_sumsCashInAndCashOutAmountsSeparately() {
        val summary = CommissionSummaryCalculator.summarize(
            transactions = listOf(
                tx(TransactionType.MTN_CASHIN, amount = 100.0, commission = 1.0),
                tx(TransactionType.MTN_CASHIN, amount = 50.0, commission = 0.5),
                tx(TransactionType.MTN_CASHOUT, amount = 20.0, commission = 0.3),
                tx(TransactionType.MTN_CASHOUT, amount = 10.0, commission = 0.15)
            ),
            startMillis = todayStart
        )

        assertEquals(150.0, summary.cashInTotal, 0.001)
        assertEquals(30.0, summary.cashOutTotal, 0.001)
        assertEquals(1.5, summary.cashInCommission, 0.001)
        assertEquals(0.45, summary.cashOutCommission, 0.001)
        assertEquals(1.95, summary.totalCommission, 0.001)
        assertEquals(180.0, summary.totalVolume, 0.001)
    }

    @Test
    fun today_ignoresFailedAndPendingTransactions() {
        val summary = CommissionSummaryCalculator.summarize(
            transactions = listOf(
                tx(TransactionType.MTN_CASHIN, amount = 100.0, commission = 1.0),
                tx(
                    TransactionType.MTN_CASHIN,
                    amount = 999.0,
                    status = TransactionStatus.FAILED
                ),
                tx(
                    TransactionType.MTN_CASHOUT,
                    amount = 888.0,
                    status = TransactionStatus.PENDING
                ),
                tx(
                    TransactionType.MTN_CASHOUT,
                    amount = 25.0,
                    commission = 0.4
                )
            ),
            startMillis = todayStart
        )

        assertEquals(100.0, summary.cashInTotal, 0.001)
        assertEquals(25.0, summary.cashOutTotal, 0.001)
        assertEquals(1.4, summary.totalCommission, 0.001)
    }

    @Test
    fun today_excludesYesterdaySuccessfulTransactions() {
        val yesterday = todayStart - 86_400_000L
        val summary = CommissionSummaryCalculator.summarize(
            transactions = listOf(
                tx(
                    TransactionType.MTN_CASHIN,
                    amount = 500.0,
                    commission = 5.0,
                    timestamp = yesterday
                ),
                tx(TransactionType.MTN_CASHOUT, amount = 40.0, commission = 0.6)
            ),
            startMillis = todayStart
        )

        assertEquals(0.0, summary.cashInTotal, 0.001)
        assertEquals(40.0, summary.cashOutTotal, 0.001)
        assertEquals(0.6, summary.totalCommission, 0.001)
    }

    @Test
    fun lifetime_includesAllSuccessfulVolumes() {
        val yesterday = todayStart - 86_400_000L
        val summary = CommissionSummaryCalculator.summarize(
            transactions = listOf(
                tx(
                    TransactionType.MTN_CASHIN,
                    amount = 500.0,
                    commission = 5.0,
                    timestamp = yesterday
                ),
                tx(TransactionType.MTN_CASHOUT, amount = 40.0, commission = 0.6)
            ),
            startMillis = todayStart,
            lifetime = true
        )

        assertEquals(500.0, summary.cashInTotal, 0.001)
        assertEquals(40.0, summary.cashOutTotal, 0.001)
        assertEquals(5.6, summary.totalCommission, 0.001)
    }
}
