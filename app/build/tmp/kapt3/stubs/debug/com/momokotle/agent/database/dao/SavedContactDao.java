package com.momokotle.agent.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.momokotle.agent.database.entity.CustomFlowEntity;
import com.momokotle.agent.database.entity.FormSubmissionEntity;
import com.momokotle.agent.database.entity.StoreOrderEntity;
import com.momokotle.agent.database.entity.ProfitRuleEntity;
import com.momokotle.agent.database.entity.SavedContactEntity;
import com.momokotle.agent.database.entity.TransactionEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00110\u0010H\'J&\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u00112\u0006\u0010\u0013\u001a\u00020\n2\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/momokotle/agent/database/dao/SavedContactDao;", "", "deleteContact", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByPhoneNumber", "Lcom/momokotle/agent/database/entity/SavedContactEntity;", "phoneNumber", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertContact", "contact", "(Lcom/momokotle/agent/database/entity/SavedContactEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "searchContacts", "query", "limit", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateContact", "app_debug"})
@androidx.room.Dao()
public abstract interface SavedContactDao {
    
    @androidx.room.Query(value = "SELECT * FROM saved_contacts ORDER BY updatedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.database.entity.SavedContactEntity>> observeAll();
    
    @androidx.room.Query(value = "SELECT * FROM saved_contacts WHERE phoneNumber = :phoneNumber LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByPhoneNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.database.entity.SavedContactEntity> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM saved_contacts\n        WHERE contactName LIKE \'%\' || :query || \'%\'\n           OR phoneNumber LIKE \'%\' || :query || \'%\'\n        ORDER BY updatedAt DESC\n        LIMIT :limit\n        ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchContacts(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.momokotle.agent.database.entity.SavedContactEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 3)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertContact(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.SavedContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateContact(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.SavedContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM saved_contacts WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContact(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}