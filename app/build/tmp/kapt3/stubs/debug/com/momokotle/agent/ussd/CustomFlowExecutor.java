package com.momokotle.agent.ussd;

import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.CustomMenuType;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.utils.AmountFormatter;
import com.momokotle.agent.utils.PhoneNumberFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u001e\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\tH\u0002J\u000e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0004J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006J \u0010\u001b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004\u00a8\u0006\u001c"}, d2 = {"Lcom/momokotle/agent/ussd/CustomFlowExecutor;", "", "()V", "amountForLaunch", "", "flow", "Lcom/momokotle/agent/models/CustomFlow;", "rawAmount", "amountForTransaction", "", "buildUssdFlow", "Lcom/momokotle/agent/ussd/UssdFlow;", "rawPhone", "formatAmountForUssd", "amount", "gridLabel", "name", "isAmountToken", "", "token", "isPhoneToken", "menuType", "Lcom/momokotle/agent/models/CustomMenuType;", "phoneForLaunch", "phoneForTransaction", "requiresAmount", "requiresPhone", "validateForRun", "app_debug"})
public final class CustomFlowExecutor {
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.ussd.CustomFlowExecutor INSTANCE = null;
    
    private CustomFlowExecutor() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.CustomMenuType menuType(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow) {
        return null;
    }
    
    public final boolean requiresPhone(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow) {
        return false;
    }
    
    public final boolean requiresAmount(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow) {
        return false;
    }
    
    /**
     * Validates home-screen phone/amount inputs for this flow.
     * @return error message, or null if valid.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateForRun(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String rawAmount) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ussd.UssdFlow buildUssdFlow(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String rawAmount) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String phoneForLaunch(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawPhone) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String amountForLaunch(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawAmount) {
        return null;
    }
    
    public final double amountForTransaction(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawAmount) {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String phoneForTransaction(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String rawPhone) {
        return null;
    }
    
    /**
     * Short label for dashboard grid (max ~2 lines).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String gridLabel(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
        return null;
    }
    
    private final boolean isPhoneToken(java.lang.String token) {
        return false;
    }
    
    private final boolean isAmountToken(java.lang.String token) {
        return false;
    }
    
    private final java.lang.String formatAmountForUssd(double amount) {
        return null;
    }
}