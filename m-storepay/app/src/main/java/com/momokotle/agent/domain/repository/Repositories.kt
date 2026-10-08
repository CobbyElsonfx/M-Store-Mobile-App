package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observeByType(type: TransactionType): Flow<List<Transaction>>
    fun observeRecent(limit: Int = 10): Flow<List<Transaction>>
    suspend fun getById(id: Long): Transaction?
    suspend fun insert(transaction: Transaction): Long
    suspend fun updateStatus(id: Long, status: TransactionStatus)
    suspend fun updateStatusAndCommission(
        id: Long,
        status: TransactionStatus,
        commission: Double,
        commissionRuleUsed: String?
    )
    suspend fun sumSuccessfulCommissionSince(type: TransactionType, startMillis: Long): Double
    suspend fun findRecentByPhoneNumbers(
        phoneNumbers: List<String>,
        cutoffTime: Long
    ): Transaction?
    suspend fun delete(id: Long)
}

interface SavedContactRepository {
    fun observeAll(): Flow<List<SavedContact>>
    suspend fun getByPhoneNumber(phoneNumber: String): SavedContact?
    suspend fun searchContacts(query: String, limit: Int = 8): List<SavedContact>
    suspend fun insertContact(contact: SavedContact): Long
    suspend fun updateContact(contact: SavedContact)
    suspend fun deleteContact(id: Long)
    suspend fun existsByPhoneNumber(phoneNumber: String): Boolean
}

interface ProfitRuleRepository {
    fun observeCustomRules(ruleType: com.momokotle.agent.models.ProfitRuleType): Flow<List<ProfitRule>>
    suspend fun replaceCustomRules(
        ruleType: com.momokotle.agent.models.ProfitRuleType,
        rules: List<ProfitRule>
    )
    suspend fun findExactCustomRule(
        ruleType: com.momokotle.agent.models.ProfitRuleType,
        amount: Double
    ): ProfitRule?

    suspend fun findTierRule(
        ruleType: com.momokotle.agent.models.ProfitRuleType,
        amount: Double
    ): ProfitRule?

    suspend fun getTierRules(ruleType: com.momokotle.agent.models.ProfitRuleType): List<ProfitRule>
}

interface CustomFlowRepository {
    fun observeAll(): Flow<List<CustomFlow>>
    suspend fun getById(id: Long): CustomFlow?
    suspend fun save(flow: CustomFlow): Long
    suspend fun delete(id: Long)
}
