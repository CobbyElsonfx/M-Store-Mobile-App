package com.momokotle.agent.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\u000e\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001e"}, d2 = {"Lcom/momokotle/agent/models/ProfitRule;", "", "id", "", "ruleType", "Lcom/momokotle/agent/models/ProfitRuleType;", "amount", "", "profitPercentage", "(JLcom/momokotle/agent/models/ProfitRuleType;DD)V", "getAmount", "()D", "getId", "()J", "getProfitPercentage", "getRuleType", "()Lcom/momokotle/agent/models/ProfitRuleType;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "profitForAmount", "toString", "", "app_debug"})
public final class ProfitRule {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitRuleType ruleType = null;
    private final double amount = 0.0;
    
    /**
     * Decimal rate: 0.01 = 1%, 0.015 = 1.5%
     */
    private final double profitPercentage = 0.0;
    
    public ProfitRule(long id, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, double amount, double profitPercentage) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitRuleType getRuleType() {
        return null;
    }
    
    public final double getAmount() {
        return 0.0;
    }
    
    /**
     * Decimal rate: 0.01 = 1%, 0.015 = 1.5%
     */
    public final double getProfitPercentage() {
        return 0.0;
    }
    
    public final double profitForAmount(double amount) {
        return 0.0;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitRuleType component2() {
        return null;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitRule copy(long id, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType, double amount, double profitPercentage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}