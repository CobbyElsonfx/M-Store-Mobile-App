package com.momokotle.agent.domain.usecase

import com.momokotle.agent.models.CommissionSummary
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType

/** Pure totals for successful Cash In / Cash Out in a time window. */
object CommissionSummaryCalculator {

    fun summarize(
        transactions: List<Transaction>,
        startMillis: Long,
        lifetime: Boolean = false
    ): CommissionSummary {
        val successful = transactions.filter {
            it.status == TransactionStatus.SUCCESSFUL &&
                (lifetime || it.timestamp >= startMillis)
        }
        val cashIn = successful.filter { it.type == TransactionType.MTN_CASHIN }
        val cashOut = successful.filter { it.type == TransactionType.MTN_CASHOUT }
        val cashInCommission = cashIn.sumOf { it.commission }
        val cashOutCommission = cashOut.sumOf { it.commission }
        return CommissionSummary(
            cashInTotal = cashIn.sumOf { it.amount },
            cashOutTotal = cashOut.sumOf { it.amount },
            cashInCommission = cashInCommission,
            cashOutCommission = cashOutCommission,
            totalCommission = cashInCommission + cashOutCommission
        )
    }
}
