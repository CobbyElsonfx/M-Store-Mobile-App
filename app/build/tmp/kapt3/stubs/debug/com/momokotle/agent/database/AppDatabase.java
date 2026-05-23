package com.momokotle.agent.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.momokotle.agent.database.dao.CustomFlowDao;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.database.dao.ProfitRuleDao;
import com.momokotle.agent.database.dao.SavedContactDao;
import com.momokotle.agent.database.dao.TransactionDao;
import com.momokotle.agent.database.entity.CustomFlowEntity;
import com.momokotle.agent.database.entity.FormSubmissionEntity;
import com.momokotle.agent.database.entity.StoreOrderEntity;
import com.momokotle.agent.database.entity.ProfitRuleEntity;
import com.momokotle.agent.database.entity.SavedContactEntity;
import com.momokotle.agent.database.entity.TransactionEntity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u000f"}, d2 = {"Lcom/momokotle/agent/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "customFlowDao", "Lcom/momokotle/agent/database/dao/CustomFlowDao;", "formSubmissionDao", "Lcom/momokotle/agent/database/dao/FormSubmissionDao;", "profitRuleDao", "Lcom/momokotle/agent/database/dao/ProfitRuleDao;", "savedContactDao", "Lcom/momokotle/agent/database/dao/SavedContactDao;", "storeOrderDao", "Lcom/momokotle/agent/database/dao/StoreOrderDao;", "transactionDao", "Lcom/momokotle/agent/database/dao/TransactionDao;", "app_debug"})
@androidx.room.Database(entities = {com.momokotle.agent.database.entity.TransactionEntity.class, com.momokotle.agent.database.entity.SavedContactEntity.class, com.momokotle.agent.database.entity.ProfitRuleEntity.class, com.momokotle.agent.database.entity.CustomFlowEntity.class, com.momokotle.agent.database.entity.StoreOrderEntity.class, com.momokotle.agent.database.entity.FormSubmissionEntity.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.TransactionDao transactionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.SavedContactDao savedContactDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.ProfitRuleDao profitRuleDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.CustomFlowDao customFlowDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.StoreOrderDao storeOrderDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.database.dao.FormSubmissionDao formSubmissionDao();
}