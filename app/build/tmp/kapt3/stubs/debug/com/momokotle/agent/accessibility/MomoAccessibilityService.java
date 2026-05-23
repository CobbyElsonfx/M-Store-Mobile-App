package com.momokotle.agent.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.momokotle.agent.models.AutomationStatus;
import com.momokotle.agent.models.UssdActionType;
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.utils.AutomationLogger;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 ?2\u00020\u0001:\u0001?B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\n\u0010!\u001a\u00060\"j\u0002`#H\u0002J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020 H\u0002J\u0010\u0010\'\u001a\u00020%2\u0006\u0010&\u001a\u00020 H\u0002J\u0010\u0010(\u001a\u00020\u000e2\u0006\u0010&\u001a\u00020 H\u0002J\u0012\u0010)\u001a\u0004\u0018\u00010 2\u0006\u0010&\u001a\u00020 H\u0002J\u0012\u0010*\u001a\u0004\u0018\u00010 2\u0006\u0010&\u001a\u00020 H\u0002J$\u0010+\u001a\u0004\u0018\u00010 2\u0006\u0010&\u001a\u00020 2\u0006\u0010,\u001a\u00020\u000e2\b\b\u0002\u0010-\u001a\u00020%H\u0002J\u0012\u0010.\u001a\u00020\u001e2\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\b\u00101\u001a\u00020\u001eH\u0016J\b\u00102\u001a\u00020\u001eH\u0016J\b\u00103\u001a\u00020\u001eH\u0014J\u001a\u00104\u001a\u00020%2\u0006\u0010&\u001a\u00020 2\b\u00105\u001a\u0004\u0018\u00010\u000eH\u0002J\u001a\u00106\u001a\u00020%2\u0006\u0010&\u001a\u00020 2\b\u00107\u001a\u0004\u0018\u00010\u000eH\u0002J\b\u00108\u001a\u00020\u001eH\u0002J(\u00109\u001a\u0004\u0018\u00010 2\u0006\u0010&\u001a\u00020 2\u0014\u0010:\u001a\u0010\u0012\u0004\u0012\u00020 \u0012\u0006\u0012\u0004\u0018\u00010 0;H\u0002J\u0010\u0010<\u001a\u00020\u001e2\u0006\u0010=\u001a\u00020\u000eH\u0002J\u000e\u0010>\u001a\u00020\u001e*\u0004\u0018\u00010 H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u0006@"}, d2 = {"Lcom/momokotle/agent/accessibility/MomoAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "debounceRunnable", "Ljava/lang/Runnable;", "engine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "getEngine", "()Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "setEngine", "(Lcom/momokotle/agent/ussd/UssdAutomationEngine;)V", "handler", "Landroid/os/Handler;", "lastProcessedText", "", "logger", "Lcom/momokotle/agent/utils/AutomationLogger;", "getLogger", "()Lcom/momokotle/agent/utils/AutomationLogger;", "setLogger", "(Lcom/momokotle/agent/utils/AutomationLogger;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "updateTransactionStatusUseCase", "Lcom/momokotle/agent/domain/usecase/UpdateTransactionStatusUseCase;", "getUpdateTransactionStatusUseCase", "()Lcom/momokotle/agent/domain/usecase/UpdateTransactionStatusUseCase;", "setUpdateTransactionStatusUseCase", "(Lcom/momokotle/agent/domain/usecase/UpdateTransactionStatusUseCase;)V", "appendTextRecursive", "", "node", "Landroid/view/accessibility/AccessibilityNodeInfo;", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "clickFirstButton", "", "root", "clickSendButton", "collectText", "findEditableField", "findFirstClickable", "findNodeByText", "text", "partial", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onDestroy", "onInterrupt", "onServiceConnected", "performClick", "targetText", "performInput", "value", "processCurrentWindow", "traverse", "matcher", "Lkotlin/Function1;", "tryVerifyTransaction", "screenText", "recycleSafely", "Companion", "app_debug"})
public final class MomoAccessibilityService extends android.accessibilityservice.AccessibilityService {
    @javax.inject.Inject()
    public com.momokotle.agent.ussd.UssdAutomationEngine engine;
    @javax.inject.Inject()
    public com.momokotle.agent.utils.AutomationLogger logger;
    @javax.inject.Inject()
    public com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase updateTransactionStatusUseCase;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastProcessedText;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Runnable debounceRunnable;
    private static final long DEBOUNCE_MS = 300L;
    private static final long STEP_DELAY_MS = 600L;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.accessibility.MomoAccessibilityService.Companion Companion = null;
    
    public MomoAccessibilityService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ussd.UssdAutomationEngine getEngine() {
        return null;
    }
    
    public final void setEngine(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdAutomationEngine p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.utils.AutomationLogger getLogger() {
        return null;
    }
    
    public final void setLogger(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.AutomationLogger p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase getUpdateTransactionStatusUseCase() {
        return null;
    }
    
    public final void setUpdateTransactionStatusUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase p0) {
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.Nullable()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    private final void processCurrentWindow() {
    }
    
    private final void tryVerifyTransaction(java.lang.String screenText) {
    }
    
    private final boolean performClick(android.view.accessibility.AccessibilityNodeInfo root, java.lang.String targetText) {
        return false;
    }
    
    private final boolean performInput(android.view.accessibility.AccessibilityNodeInfo root, java.lang.String value) {
        return false;
    }
    
    private final boolean clickSendButton(android.view.accessibility.AccessibilityNodeInfo root) {
        return false;
    }
    
    private final boolean clickFirstButton(android.view.accessibility.AccessibilityNodeInfo root) {
        return false;
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo findNodeByText(android.view.accessibility.AccessibilityNodeInfo root, java.lang.String text, boolean partial) {
        return null;
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo findEditableField(android.view.accessibility.AccessibilityNodeInfo root) {
        return null;
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo findFirstClickable(android.view.accessibility.AccessibilityNodeInfo root) {
        return null;
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo traverse(android.view.accessibility.AccessibilityNodeInfo root, kotlin.jvm.functions.Function1<? super android.view.accessibility.AccessibilityNodeInfo, ? extends android.view.accessibility.AccessibilityNodeInfo> matcher) {
        return null;
    }
    
    private final java.lang.String collectText(android.view.accessibility.AccessibilityNodeInfo root) {
        return null;
    }
    
    private final void appendTextRecursive(android.view.accessibility.AccessibilityNodeInfo node, java.lang.StringBuilder builder) {
    }
    
    private final void recycleSafely(android.view.accessibility.AccessibilityNodeInfo $this$recycleSafely) {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/momokotle/agent/accessibility/MomoAccessibilityService$Companion;", "", "()V", "DEBOUNCE_MS", "", "STEP_DELAY_MS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}