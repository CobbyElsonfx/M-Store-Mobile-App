package com.momokotle.agent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.momokotle.agent.database.dao.CustomFlowDao
import com.momokotle.agent.database.dao.FormSubmissionDao
import com.momokotle.agent.database.dao.StoreOrderDao
import com.momokotle.agent.database.dao.ProfitRuleDao
import com.momokotle.agent.database.dao.SavedContactDao
import com.momokotle.agent.database.dao.TransactionDao
import com.momokotle.agent.database.entity.CustomFlowEntity
import com.momokotle.agent.database.entity.FormSubmissionEntity
import com.momokotle.agent.database.entity.StoreOrderEntity
import com.momokotle.agent.database.entity.ProfitRuleEntity
import com.momokotle.agent.database.entity.SavedContactEntity
import com.momokotle.agent.database.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        SavedContactEntity::class,
        ProfitRuleEntity::class,
        CustomFlowEntity::class,
        StoreOrderEntity::class,
        FormSubmissionEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun savedContactDao(): SavedContactDao
    abstract fun profitRuleDao(): ProfitRuleDao
    abstract fun customFlowDao(): CustomFlowDao
    abstract fun storeOrderDao(): StoreOrderDao
    abstract fun formSubmissionDao(): FormSubmissionDao
}
