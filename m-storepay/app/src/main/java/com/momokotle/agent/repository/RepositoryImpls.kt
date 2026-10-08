package com.momokotle.agent.repository

import com.momokotle.agent.database.dao.CustomFlowDao
import com.momokotle.agent.database.dao.ProfitRuleDao
import com.momokotle.agent.database.dao.SavedContactDao
import com.momokotle.agent.database.dao.TransactionDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.repository.ProfitRuleRepository
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.utils.ProfitAmountMatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun observeAll(): Flow<List<Transaction>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeByType(type: TransactionType): Flow<List<Transaction>> =
        dao.observeByType(type.name).map { list -> list.map { it.toDomain() } }

    override fun observeRecent(limit: Int): Flow<List<Transaction>> =
        dao.observeRecent(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Transaction? =
        dao.getById(id)?.toDomain()

    override suspend fun insert(transaction: Transaction): Long =
        dao.insert(transaction.toEntity())

    override suspend fun updateStatus(id: Long, status: TransactionStatus) {
        dao.updateStatus(id, status.name)
    }

    override suspend fun updateStatusAndCommission(
        id: Long,
        status: TransactionStatus,
        commission: Double,
        commissionRuleUsed: String?
    ) {
        dao.updateStatusAndCommission(
            id = id,
            status = status.name,
            profit = commission,
            commission = commission,
            commissionRuleUsed = commissionRuleUsed
        )
    }

    override suspend fun sumSuccessfulCommissionSince(
        type: TransactionType,
        startMillis: Long
    ): Double = dao.sumSuccessfulCommissionSince(type.name, startMillis)

    override suspend fun findRecentByPhoneNumbers(
        phoneNumbers: List<String>,
        cutoffTime: Long
    ): Transaction? {
        if (phoneNumbers.isEmpty()) return null
        return dao.findRecentByPhoneNumbers(phoneNumbers, cutoffTime)?.toDomain()
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }
}

@Singleton
class SavedContactRepositoryImpl @Inject constructor(
    private val dao: SavedContactDao
) : SavedContactRepository {

    override fun observeAll(): Flow<List<SavedContact>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getByPhoneNumber(phoneNumber: String): SavedContact? =
        dao.getByPhoneNumber(phoneNumber)?.toDomain()

    override suspend fun searchContacts(query: String, limit: Int): List<SavedContact> =
        dao.searchContacts(query.trim(), limit).map { it.toDomain() }

    override suspend fun insertContact(contact: SavedContact): Long =
        dao.insertContact(contact.toEntity())

    override suspend fun updateContact(contact: SavedContact) {
        dao.updateContact(contact.toEntity())
    }

    override suspend fun deleteContact(id: Long) {
        dao.deleteContact(id)
    }

    override suspend fun existsByPhoneNumber(phoneNumber: String): Boolean =
        dao.getByPhoneNumber(phoneNumber) != null
}

@Singleton
class ProfitRuleRepositoryImpl @Inject constructor(
    private val dao: ProfitRuleDao
) : ProfitRuleRepository {

    override fun observeCustomRules(ruleType: ProfitRuleType): Flow<List<ProfitRule>> =
        dao.observeByType(ruleType.name).map { list -> list.map { it.toDomain() } }

    override suspend fun replaceCustomRules(ruleType: ProfitRuleType, rules: List<ProfitRule>) {
        dao.deleteAllByType(ruleType.name)
        rules.forEach { rule ->
            dao.insert(
                rule.copy(ruleType = ruleType).toEntity().copy(id = 0)
            )
        }
    }

    override suspend fun findExactCustomRule(ruleType: ProfitRuleType, amount: Double): ProfitRule? {
        val normalized = ProfitAmountMatcher.normalize(amount)
        return dao.getAllByType(ruleType.name)
            .map { it.toDomain() }
            .firstOrNull { ProfitAmountMatcher.matches(it.minAmount, normalized) && it.minAmount == it.maxAmount }
    }

    override suspend fun findTierRule(ruleType: ProfitRuleType, amount: Double): ProfitRule? {
        val normalized = ProfitAmountMatcher.normalize(amount)
        return dao.getAllByType(ruleType.name)
            .map { it.toDomain() }
            .firstOrNull { it.contains(normalized) }
    }

    override suspend fun getTierRules(ruleType: ProfitRuleType): List<ProfitRule> =
        dao.getAllByType(ruleType.name).map { it.toDomain() }
}

@Singleton
class CustomFlowRepositoryImpl @Inject constructor(
    private val dao: CustomFlowDao
) : CustomFlowRepository {

    override fun observeAll(): Flow<List<CustomFlow>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): CustomFlow? =
        dao.getById(id)?.toDomain()

    override suspend fun save(flow: CustomFlow): Long =
        dao.insert(flow.toEntity())

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }
}
