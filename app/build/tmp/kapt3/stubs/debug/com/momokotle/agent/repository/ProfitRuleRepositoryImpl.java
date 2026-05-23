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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u000e0\r2\u0006\u0010\u0007\u001a\u00020\bH\u0016J$\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/momokotle/agent/repository/ProfitRuleRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;", "dao", "Lcom/momokotle/agent/database/dao/ProfitRuleDao;", "(Lcom/momokotle/agent/database/dao/ProfitRuleDao;)V", "findExactCustomRule", "Lcom/momokotle/agent/models/ProfitRule;", "ruleType", "Lcom/momokotle/agent/models/ProfitRuleType;", "amount", "", "(Lcom/momokotle/agent/models/ProfitRuleType;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeCustomRules", "Lkotlinx/coroutines/flow/Flow;", "", "replaceCustomRules", "", "rules", "(Lcom/momokotle/agent/models/ProfitRuleType;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProfitRuleRepositoryImpl implements com.momokotle.agent.domain.repository.ProfitRuleRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.ProfitRuleDao dao = null;
    
    @javax.inject.Inject()
    public ProfitRuleRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.ProfitRuleDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.ProfitRule>> observeCustomRules(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object replaceCustomRules(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> rules, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object findExactCustomRule(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.ProfitRule> $completion) {
        return null;
    }
}