package com.momokotle.agent.repository;

import com.momokotle.agent.database.dao.CustomFlowDao;
import com.momokotle.agent.database.dao.ProfitRuleDao;
import com.momokotle.agent.database.dao.SavedContactDao;
import com.momokotle.agent.database.dao.TransactionDao;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.ProfitRule;
import com.momokotle.agent.models.ProfitRuleType;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.utils.ProfitAmountMatcher;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u000e0\rH\u0016J\u0016\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/momokotle/agent/repository/CustomFlowRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/CustomFlowRepository;", "dao", "Lcom/momokotle/agent/database/dao/CustomFlowDao;", "(Lcom/momokotle/agent/database/dao/CustomFlowDao;)V", "delete", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "Lcom/momokotle/agent/models/CustomFlow;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "save", "flow", "(Lcom/momokotle/agent/models/CustomFlow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CustomFlowRepositoryImpl implements com.momokotle.agent.domain.repository.CustomFlowRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.CustomFlowDao dao = null;
    
    @javax.inject.Inject()
    public CustomFlowRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.CustomFlowDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.CustomFlow>> observeAll() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.CustomFlow> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object save(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object delete(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}