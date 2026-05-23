package com.momokotle.agent.ussd;

import com.momokotle.agent.models.AutomationStatus;
import com.momokotle.agent.models.UssdActionType;
import com.momokotle.agent.utils.AutomationLogger;
import com.momokotle.agent.utils.UssdSecurityHelper;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0016\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010%\u001a\u00020&J\u0006\u0010\'\u001a\u00020&J\r\u0010(\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\u0002\u0010\u0012J\b\u0010)\u001a\u0004\u0018\u00010*J\u0006\u0010+\u001a\u00020&J\u0010\u0010,\u001a\u00020\u001b2\b\b\u0002\u0010-\u001a\u00020\u000fJ\u000e\u0010.\u001a\u00020&2\u0006\u0010/\u001a\u00020\tJ\u0006\u00100\u001a\u00020&J\u0016\u00101\u001a\u00020\u001b2\u0006\u00102\u001a\u00020\t2\u0006\u00103\u001a\u00020*J\u0006\u00104\u001a\u00020\u001bJ\b\u00105\u001a\u00020&H\u0002J\u0010\u00106\u001a\u0004\u0018\u00010\t2\u0006\u00103\u001a\u00020*J\u0006\u00107\u001a\u00020&J\u000e\u00108\u001a\u00020\u001b2\u0006\u00109\u001a\u00020\tJ1\u0010:\u001a\u00020&2\u0006\u0010;\u001a\u00020\r2\u0006\u0010<\u001a\u00020\u00192\n\b\u0002\u0010=\u001a\u0004\u0018\u00010\u000f2\b\b\u0002\u0010#\u001a\u00020\u001b\u00a2\u0006\u0002\u0010>J\u0010\u0010?\u001a\u00020\t2\u0006\u00103\u001a\u00020*H\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R$\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f@BX\u0086\u000e\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001a\u001a\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001cR\u0019\u0010\u001d\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000b0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010#\u001a\u00020\u001b2\u0006\u0010\u000e\u001a\u00020\u001b@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001c\u00a8\u0006@"}, d2 = {"Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "", "logger", "Lcom/momokotle/agent/utils/AutomationLogger;", "(Lcom/momokotle/agent/utils/AutomationLogger;)V", "_currentStepIndex", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_lastError", "", "_status", "Lcom/momokotle/agent/models/AutomationStatus;", "activeFlow", "Lcom/momokotle/agent/ussd/UssdFlow;", "<set-?>", "", "activeTransactionId", "getActiveTransactionId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "currentStepIndex", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentStepIndex", "()Lkotlinx/coroutines/flow/StateFlow;", "flowContext", "Lcom/momokotle/agent/ussd/UssdFlowContext;", "isRunning", "", "()Z", "lastError", "getLastError", "startedAt", "status", "getStatus", "stepAttempts", "verificationEnabled", "getVerificationEnabled", "advanceStep", "", "cancel", "consumeTransactionId", "currentStep", "Lcom/momokotle/agent/ussd/UssdStep;", "handlePinStep", "isTimedOut", "maxDurationMs", "markFailed", "message", "markSuccess", "matchesExpected", "screenText", "step", "recordStepAttempt", "reset", "resolveInputValue", "resumeAfterUserInput", "shouldStopForPin", "text", "start", "flow", "context", "transactionId", "(Lcom/momokotle/agent/ussd/UssdFlow;Lcom/momokotle/agent/ussd/UssdFlowContext;Ljava/lang/Long;Z)V", "stepLabel", "app_debug"})
public final class UssdAutomationEngine {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.AutomationLogger logger = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.models.AutomationStatus> _status = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.AutomationStatus> status = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _currentStepIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> currentStepIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _lastError = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> lastError = null;
    @org.jetbrains.annotations.Nullable()
    private com.momokotle.agent.ussd.UssdFlow activeFlow;
    @org.jetbrains.annotations.NotNull()
    private com.momokotle.agent.ussd.UssdFlowContext flowContext;
    private int stepAttempts = 0;
    private long startedAt = 0L;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Long activeTransactionId;
    private boolean verificationEnabled = false;
    
    public UssdAutomationEngine(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.AutomationLogger logger) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.AutomationStatus> getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCurrentStepIndex() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getLastError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getActiveTransactionId() {
        return null;
    }
    
    public final boolean getVerificationEnabled() {
        return false;
    }
    
    public final boolean isRunning() {
        return false;
    }
    
    public final void start(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdFlow flow, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdFlowContext context, @org.jetbrains.annotations.Nullable()
    java.lang.Long transactionId, boolean verificationEnabled) {
    }
    
    public final void cancel() {
    }
    
    public final void markSuccess() {
    }
    
    public final void markFailed(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.ussd.UssdStep currentStep() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String resolveInputValue(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdStep step) {
        return null;
    }
    
    public final void advanceStep() {
    }
    
    public final boolean recordStepAttempt() {
        return false;
    }
    
    public final void handlePinStep() {
    }
    
    public final void resumeAfterUserInput() {
    }
    
    public final boolean matchesExpected(@org.jetbrains.annotations.NotNull()
    java.lang.String screenText, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdStep step) {
        return false;
    }
    
    public final boolean shouldStopForPin(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
    
    public final boolean isTimedOut(long maxDurationMs) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long consumeTransactionId() {
        return null;
    }
    
    private final java.lang.String stepLabel(com.momokotle.agent.ussd.UssdStep step) {
        return null;
    }
    
    private final void reset() {
    }
}