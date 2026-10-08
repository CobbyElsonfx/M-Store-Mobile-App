package com.momokotle.agent.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.momokotle.agent.database.entity.CustomFlowEntity;
import com.momokotle.agent.database.entity.FormSubmissionEntity;
import com.momokotle.agent.database.entity.StoreOrderEntity;
import com.momokotle.agent.database.entity.StoreProductEntity;
import com.momokotle.agent.database.entity.ProfitRuleEntity;
import com.momokotle.agent.database.entity.SavedContactEntity;
import com.momokotle.agent.database.entity.TransactionEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ \u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00150\u00142\u0006\u0010\u0004\u001a\u00020\u0005H\'\u00a8\u0006\u0016"}, d2 = {"Lcom/momokotle/agent/database/dao/ProfitRuleDao;", "", "deleteAllByType", "", "ruleType", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByTypeAndAmount", "Lcom/momokotle/agent/database/entity/ProfitRuleEntity;", "amount", "", "(Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "rule", "(Lcom/momokotle/agent/database/entity/ProfitRuleEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeByType", "Lkotlinx/coroutines/flow/Flow;", "", "app_debug"})
@androidx.room.Dao()
public abstract interface ProfitRuleDao {
    
    @androidx.room.Query(value = "SELECT * FROM profit_rules WHERE ruleType = :ruleType ORDER BY amount ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.database.entity.ProfitRuleEntity>> observeByType(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleType);
    
    @androidx.room.Query(value = "SELECT * FROM profit_rules WHERE ruleType = :ruleType AND amount = :amount LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByTypeAndAmount(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleType, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.database.entity.ProfitRuleEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.ProfitRuleEntity rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "DELETE FROM profit_rules WHERE ruleType = :ruleType")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllByType(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleType, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM profit_rules WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}