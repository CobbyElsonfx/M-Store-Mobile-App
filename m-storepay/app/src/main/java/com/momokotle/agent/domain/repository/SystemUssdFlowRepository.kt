package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.ussd.UssdFlow

interface SystemUssdFlowRepository {
    suspend fun getFlow(type: TransactionType): UssdFlow
    suspend fun refreshFlows()
}
