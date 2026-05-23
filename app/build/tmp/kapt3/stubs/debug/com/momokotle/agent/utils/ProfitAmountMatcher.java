package com.momokotle.agent.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006\u00a8\u0006\n"}, d2 = {"Lcom/momokotle/agent/utils/ProfitAmountMatcher;", "", "()V", "matches", "", "a", "", "b", "normalize", "amount", "app_debug"})
public final class ProfitAmountMatcher {
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.ProfitAmountMatcher INSTANCE = null;
    
    private ProfitAmountMatcher() {
        super();
    }
    
    public final double normalize(double amount) {
        return 0.0;
    }
    
    public final boolean matches(double a, double b) {
        return false;
    }
}