package com.momokotle.agent.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004J\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0004\u00a8\u0006\r"}, d2 = {"Lcom/momokotle/agent/utils/PercentFormatter;", "", "()V", "calculateProfit", "", "amount", "decimalRate", "parsePercentInput", "input", "", "(Ljava/lang/String;)Ljava/lang/Double;", "toDisplayPercent", "toLabel", "app_debug"})
public final class PercentFormatter {
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.PercentFormatter INSTANCE = null;
    
    private PercentFormatter() {
        super();
    }
    
    /**
     * Parses user input like "1", "1.5", or "1%" into decimal rate (0.01, 0.015).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double parsePercentInput(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    /**
     * Decimal rate (0.01) to display percent string ("1").
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toDisplayPercent(double decimalRate) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toLabel(double decimalRate) {
        return null;
    }
    
    public final double calculateProfit(double amount, double decimalRate) {
        return 0.0;
    }
}