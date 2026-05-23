package com.momokotle.agent.utils;

import android.util.Log;
import kotlinx.coroutines.flow.StateFlow;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fR\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/momokotle/agent/utils/AutomationLogger;", "", "()V", "_logs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/momokotle/agent/utils/AutomationLogEntry;", "logs", "Lkotlinx/coroutines/flow/StateFlow;", "getLogs", "()Lkotlinx/coroutines/flow/StateFlow;", "clear", "", "log", "message", "", "Companion", "app_debug"})
public final class AutomationLogger {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.momokotle.agent.utils.AutomationLogEntry>> _logs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.utils.AutomationLogEntry>> logs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "MomoAutomation";
    private static final int MAX_LOGS = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.AutomationLogger.Companion Companion = null;
    
    @javax.inject.Inject()
    public AutomationLogger() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.utils.AutomationLogEntry>> getLogs() {
        return null;
    }
    
    public final void log(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    public final void clear() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/momokotle/agent/utils/AutomationLogger$Companion;", "", "()V", "MAX_LOGS", "", "TAG", "", "formatTimestamp", "timestamp", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String formatTimestamp(long timestamp) {
            return null;
        }
    }
}