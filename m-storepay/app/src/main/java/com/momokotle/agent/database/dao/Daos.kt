package com.momokotle.agent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.momokotle.agent.database.entity.CustomFlowEntity
import com.momokotle.agent.database.entity.FormSubmissionEntity
import com.momokotle.agent.database.entity.StoreOrderEntity
import com.momokotle.agent.database.entity.StoreProductEntity
import com.momokotle.agent.database.entity.ProfitRuleEntity
import com.momokotle.agent.database.entity.SavedContactEntity
import com.momokotle.agent.database.entity.SystemUssdFlowEntity
import com.momokotle.agent.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun observeByType(type: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Query("UPDATE transactions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query(
        """
        UPDATE transactions SET status = :status, profit = :profit, commission = :commission,
        commissionRuleUsed = :commissionRuleUsed WHERE id = :id
        """
    )
    suspend fun updateStatusAndCommission(
        id: Long,
        status: String,
        profit: Double,
        commission: Double,
        commissionRuleUsed: String?
    )

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TransactionEntity?

    /**
     * Most recent Room transaction for any of the given phone variants
     * (normalized local forms) within [cutoffTime]..now.
     */
    @Query(
        """
        SELECT * FROM transactions
        WHERE phoneNumber IN (:phoneNumbers)
          AND timestamp >= :cutoffTime
        ORDER BY timestamp DESC
        LIMIT 1
        """
    )
    suspend fun findRecentByPhoneNumbers(
        phoneNumbers: List<String>,
        cutoffTime: Long
    ): TransactionEntity?

    @Query(
        """
        SELECT COUNT(*) FROM transactions
        WHERE phoneNumber IN (:phoneNumbers)
        """
    )
    suspend fun countByPhoneNumbers(phoneNumbers: List<String>): Int

    /**
     * Phones that appear often in history and match a typed query.
     * Used to suggest / promote frequently used customer numbers.
     */
    @Query(
        """
        SELECT phoneNumber FROM transactions
        WHERE phoneNumber IS NOT NULL
          AND phoneNumber != ''
          AND phoneNumber LIKE '%' || :query || '%'
        GROUP BY phoneNumber
        HAVING COUNT(*) >= :minCount
        ORDER BY MAX(timestamp) DESC
        LIMIT :limit
        """
    )
    suspend fun findFrequentPhonesMatching(
        query: String,
        minCount: Int,
        limit: Int
    ): List<String>

    @Query(
        """
        SELECT COALESCE(SUM(commission), 0) FROM transactions
        WHERE status = 'SUCCESSFUL' AND timestamp >= :startMillis AND type = :type
        """
    )
    suspend fun sumSuccessfulCommissionSince(type: String, startMillis: Long): Double

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface SavedContactDao {
    @Query("SELECT * FROM saved_contacts ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<SavedContactEntity>>

    @Query("SELECT * FROM saved_contacts WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getByPhoneNumber(phoneNumber: String): SavedContactEntity?

    @Query(
        """
        SELECT * FROM saved_contacts
        WHERE contactName LIKE '%' || :query || '%'
           OR phoneNumber LIKE '%' || :query || '%'
        ORDER BY updatedAt DESC
        LIMIT :limit
        """
    )
    suspend fun searchContacts(query: String, limit: Int = 8): List<SavedContactEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertContact(contact: SavedContactEntity): Long

    @Update
    suspend fun updateContact(contact: SavedContactEntity)

    @Query("DELETE FROM saved_contacts WHERE id = :id")
    suspend fun deleteContact(id: Long)
}

@Dao
interface ProfitRuleDao {
    @Query("SELECT * FROM profit_rules WHERE ruleType = :ruleType ORDER BY minAmount ASC")
    fun observeByType(ruleType: String): Flow<List<ProfitRuleEntity>>

    @Query("SELECT * FROM profit_rules WHERE ruleType = :ruleType ORDER BY minAmount ASC")
    suspend fun getAllByType(ruleType: String): List<ProfitRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: ProfitRuleEntity): Long

    @Query("DELETE FROM profit_rules WHERE ruleType = :ruleType")
    suspend fun deleteAllByType(ruleType: String)

    @Query("DELETE FROM profit_rules WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface StoreProductDao {
    @Query("SELECT * FROM store_products ORDER BY productName ASC")
    fun observeAll(): Flow<List<StoreProductEntity>>

    @Query(
        """
        SELECT * FROM store_products
        WHERE category = :category
        ORDER BY productName ASC
        """
    )
    fun observeByCategory(category: String): Flow<List<StoreProductEntity>>

    @Query("SELECT * FROM store_products WHERE productId = :productId LIMIT 1")
    suspend fun getById(productId: String): StoreProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<StoreProductEntity>)

    @Query("DELETE FROM store_products")
    suspend fun deleteAll()
}

@Dao
interface StoreOrderDao {
    @Query("SELECT * FROM store_orders ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<StoreOrderEntity>>

    @Query("SELECT * FROM store_orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUserId(userId: String): Flow<List<StoreOrderEntity>>

    @Query("SELECT * FROM store_orders WHERE orderId = :orderId LIMIT 1")
    suspend fun getById(orderId: String): StoreOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: StoreOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<StoreOrderEntity>)

    @Query("DELETE FROM store_orders WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)
}

@Dao
interface FormSubmissionDao {
    @Query("SELECT * FROM form_submissions WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUserId(userId: String): Flow<List<FormSubmissionEntity>>

    @Query("SELECT * FROM form_submissions WHERE submissionId = :submissionId LIMIT 1")
    suspend fun getById(submissionId: String): FormSubmissionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(submission: FormSubmissionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(submissions: List<FormSubmissionEntity>)

    @Query("DELETE FROM form_submissions WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)
}

@Dao
interface CustomFlowDao {
    @Query("SELECT * FROM custom_flows ORDER BY name ASC")
    fun observeAll(): Flow<List<CustomFlowEntity>>

    @Query("SELECT * FROM custom_flows WHERE id = :id")
    suspend fun getById(id: Long): CustomFlowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flow: CustomFlowEntity): Long

    @Query("DELETE FROM custom_flows WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface SystemUssdFlowDao {
    @Query("SELECT * FROM system_ussd_flows WHERE flowKey = :flowKey LIMIT 1")
    suspend fun getByFlowKey(flowKey: String): SystemUssdFlowEntity?

    @Query("SELECT * FROM system_ussd_flows")
    suspend fun getAll(): List<SystemUssdFlowEntity>

    @Query("SELECT COUNT(*) FROM system_ussd_flows")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(flows: List<SystemUssdFlowEntity>)

    @Query("DELETE FROM system_ussd_flows")
    suspend fun deleteAll()
}
