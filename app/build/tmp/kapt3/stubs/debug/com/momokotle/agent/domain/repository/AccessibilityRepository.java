package com.momokotle.agent.domain.repository;

import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0007\u001a\u00020\u0004H&J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\tH&R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000b"}, d2 = {"Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "", "accessibilityStatus", "Lkotlinx/coroutines/flow/StateFlow;", "", "getAccessibilityStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "isAccessibilityEnabled", "openAccessibilitySettings", "", "refresh", "app_debug"})
public abstract interface AccessibilityRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getAccessibilityStatus();
    
    public abstract boolean isAccessibilityEnabled();
    
    public abstract void refresh();
    
    public abstract void openAccessibilitySettings();
}