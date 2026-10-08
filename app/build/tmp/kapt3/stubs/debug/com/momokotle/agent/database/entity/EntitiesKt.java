package com.momokotle.agent.database.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.momokotle.agent.models.ContactNetwork;
import com.momokotle.agent.models.AgentFormType;
import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatusMapper;
import com.momokotle.agent.models.OrderStatusMapper;
import com.momokotle.agent.models.StoreOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.momokotle.agent.models.ProfitRuleType;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.models.TransactionType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\u0002\u001a\u00020\u0003*\u00020\u0004\u001a\f\u0010\u0002\u001a\u0004\u0018\u00010\u0005*\u00020\u0006\u001a\n\u0010\u0002\u001a\u00020\u0007*\u00020\b\u001a\n\u0010\u0002\u001a\u00020\t*\u00020\n\u001a\n\u0010\u0002\u001a\u00020\u000b*\u00020\f\u001a\n\u0010\u0002\u001a\u00020\r*\u00020\u000e\u001a\n\u0010\u000f\u001a\u00020\u0004*\u00020\u0003\u001a\n\u0010\u000f\u001a\u00020\u0006*\u00020\u0005\u001a\n\u0010\u000f\u001a\u00020\b*\u00020\u0007\u001a\n\u0010\u000f\u001a\u00020\n*\u00020\t\u001a\n\u0010\u000f\u001a\u00020\f*\u00020\u000b\u001a\n\u0010\u000f\u001a\u00020\u000e*\u00020\r\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"gson", "Lcom/google/gson/Gson;", "toDomain", "Lcom/momokotle/agent/models/CustomFlow;", "Lcom/momokotle/agent/database/entity/CustomFlowEntity;", "Lcom/momokotle/agent/models/FormSubmission;", "Lcom/momokotle/agent/database/entity/FormSubmissionEntity;", "Lcom/momokotle/agent/models/ProfitRule;", "Lcom/momokotle/agent/database/entity/ProfitRuleEntity;", "Lcom/momokotle/agent/models/SavedContact;", "Lcom/momokotle/agent/database/entity/SavedContactEntity;", "Lcom/momokotle/agent/models/StoreOrder;", "Lcom/momokotle/agent/database/entity/StoreOrderEntity;", "Lcom/momokotle/agent/models/Transaction;", "Lcom/momokotle/agent/database/entity/TransactionEntity;", "toEntity", "app_debug"})
public final class EntitiesKt {
    @org.jetbrains.annotations.NotNull()
    private static final com.google.gson.Gson gson = null;
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.Transaction toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.TransactionEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.TransactionEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.Transaction $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.SavedContact toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.SavedContactEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.SavedContactEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SavedContact $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.ProfitRule toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.ProfitRuleEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.ProfitRuleEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRule $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.CustomFlow toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.CustomFlowEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.CustomFlowEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.StoreOrder toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.StoreOrderEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.StoreOrderEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreOrder $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final com.momokotle.agent.models.FormSubmission toDomain(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.entity.FormSubmissionEntity $this$toDomain) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.database.entity.FormSubmissionEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.FormSubmission $this$toEntity) {
        return null;
    }
}