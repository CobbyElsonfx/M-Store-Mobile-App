package com.momokotle.agent.domain.repository;

import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.ProfitRule;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.models.TransactionType;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u000b0\n2\u0006\u0010\u0004\u001a\u00020\u0005H&J$\u0010\f\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u00a6@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;", "", "findExactCustomRule", "Lcom/momokotle/agent/models/ProfitRule;", "ruleType", "Lcom/momokotle/agent/models/ProfitRuleType;", "amount", "", "(Lcom/momokotle/agent/models/ProfitRuleType;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeCustomRules", "Lkotlinx/coroutines/flow/Flow;", "", "replaceCustomRules", "", "rules", "(Lcom/momokotle/agent/models/ProfitRuleType;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ProfitRuleRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.ProfitRule>> observeCustomRules(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object replaceCustomRules(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> rules, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findExactCustomRule(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.ProfitRule> $completion);
}