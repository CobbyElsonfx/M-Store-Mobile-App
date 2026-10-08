package com.momokotle.agent.repository

import com.google.gson.Gson
import com.momokotle.agent.data.datastore.UssdFlowCacheDataStore
import com.momokotle.agent.data.remote.ApiUssdFlowDto
import com.momokotle.agent.data.remote.MStorepayApiService
import com.momokotle.agent.data.remote.UssdFlowRemoteMapper
import com.momokotle.agent.database.dao.SystemUssdFlowDao
import com.momokotle.agent.database.entity.toSystemEntity
import com.momokotle.agent.database.entity.toUssdFlow
import com.momokotle.agent.domain.repository.SystemUssdFlowRepository
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.ussd.UssdFlows
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Built-in USSD flows:
 * 1. Room (primary, offline) — used for every transaction
 * 2. Hardcoded [UssdFlows] fallback if Room has no row for that type
 *
 * [refreshFlows] pulls from the API in the background (e.g. dashboard resume)
 * and writes into Room. It is never called from [getFlow].
 */
@Singleton
class SystemUssdFlowRepositoryImpl @Inject constructor(
    private val api: MStorepayApiService,
    private val dao: SystemUssdFlowDao,
    private val legacyCache: UssdFlowCacheDataStore,
    private val gson: Gson
) : SystemUssdFlowRepository {

    override suspend fun getFlow(type: TransactionType): UssdFlow {
        ensureLocalCatalog()
        val cached = dao.getByFlowKey(type.name)?.toUssdFlow(gson)

        // Drop stale TEMP *170# Cash In rows so agent *171# is restored after testing.
        if (type == TransactionType.MTN_CASHIN &&
            (cached == null || cached.ussdCode == "*170#")
        ) {
            val builtIn = UssdFlows.CashInFlow
            runCatching { dao.upsertAll(listOf(builtIn.toSystemEntity(gson))) }
            return builtIn
        }

        // Keep Airtime / Balance / Commission in sync with built-in agent menus.
        if (type in FORCE_SYNC_FLOW_TYPES) {
            val builtIn = UssdFlows.forTransactionType(type)
            if (cached == null || !sameStepTokens(cached, builtIn)) {
                runCatching { dao.upsertAll(listOf(builtIn.toSystemEntity(gson))) }
                return builtIn
            }
            return cached
        }

        if (cached != null) return cached

        // Missing or corrupt Room row (e.g. minify-broken JSON) — use built-in and repair.
        val fallback = UssdFlows.forTransactionType(type)
        runCatching {
            dao.upsertAll(listOf(fallback.toSystemEntity(gson)))
        }
        return fallback
    }

    override suspend fun refreshFlows() {
        ensureLocalCatalog()
        runCatching {
            val response = api.listUssdFlows()
            persistDtos(response.data)
            legacyCache.clear()
        }
    }

    /**
     * Seeds Room from legacy DataStore cache or hardcoded defaults when empty
     * or when existing rows fail Gson validation (corrupt minify cache).
     * Never hits the network.
     */
    private suspend fun ensureLocalCatalog() {
        if (dao.count() > 0 && hasValidCatalog()) return

        if (dao.count() > 0) {
            dao.deleteAll()
        }

        val fromLegacy = legacyCache.getFlows().orEmpty()
            .mapNotNull { UssdFlowRemoteMapper.map(it) }
        if (fromLegacy.isNotEmpty()) {
            persistFlows(fromLegacy)
            if (hasValidCatalog()) return
            dao.deleteAll()
        }

        persistFlows(defaultFlows())
    }

    private suspend fun hasValidCatalog(): Boolean {
        val sample = defaultFlows().firstOrNull() ?: return false
        return dao.getByFlowKey(sample.transactionType.name)?.toUssdFlow(gson) != null
    }

    private suspend fun persistDtos(dtos: List<ApiUssdFlowDto>) {
        val flows = dtos.mapNotNull { UssdFlowRemoteMapper.map(it) }
        if (flows.isNotEmpty()) {
            persistFlows(flows)
        }
    }

    private suspend fun persistFlows(flows: List<UssdFlow>) {
        val now = System.currentTimeMillis()
        dao.upsertAll(flows.map { it.toSystemEntity(gson, now) })
    }

    private fun defaultFlows(): List<UssdFlow> = listOf(
        UssdFlows.CashInFlow,
        UssdFlows.CashOutFlow,
        UssdFlows.PayToMerchantFlow,
        UssdFlows.TransferAirtimeFlow,
        UssdFlows.BalanceFlow,
        UssdFlows.CommissionFlow,
        UssdFlows.EvdJust4uFlow
    )

    private fun sameStepTokens(a: UssdFlow, b: UssdFlow): Boolean =
        a.steps.map { it.inputValue } == b.steps.map { it.inputValue }

    companion object {
        private val FORCE_SYNC_FLOW_TYPES = setOf(
            TransactionType.MTN_AIRTIME,
            TransactionType.BALANCE,
            TransactionType.COMMISSION
        )
    }
}
