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
    @Query("SELECT * FROM profit_rules WHERE ruleType = :ruleType ORDER BY amount ASC")
    fun observeByType(ruleType: String): Flow<List<ProfitRuleEntity>>

    @Query("SELECT * FROM profit_rules WHERE ruleType = :ruleType AND amount = :amount LIMIT 1")
    suspend fun getByTypeAndAmount(ruleType: String, amount: Double): ProfitRuleEntity?

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
        WHERE category = :category AND isAvailable = 1
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
