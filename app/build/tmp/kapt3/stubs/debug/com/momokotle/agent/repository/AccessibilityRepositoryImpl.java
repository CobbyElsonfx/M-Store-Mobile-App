package com.momokotle.agent.repository;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.momokotle.agent.accessibility.MomoAccessibilityService;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.utils.AccessibilityHelper;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000e\u001a\u00020\u0007H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/momokotle/agent/repository/AccessibilityRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_accessibilityStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "accessibilityStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getAccessibilityStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "settingsObserver", "Landroid/database/ContentObserver;", "isAccessibilityEnabled", "openAccessibilitySettings", "", "refresh", "app_debug"})
public final class AccessibilityRepositoryImpl implements com.momokotle.agent.domain.repository.AccessibilityRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _accessibilityStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> accessibilityStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final android.database.ContentObserver settingsObserver = null;
    
    @javax.inject.Inject()
    public AccessibilityRepositoryImpl(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getAccessibilityStatus() {
        return null;
    }
    
    @java.lang.Override()
    public boolean isAccessibilityEnabled() {
        return false;
    }
    
    @java.lang.Override()
    public void refresh() {
    }
    
    @java.lang.Override()
    public void openAccessibilitySettings() {
    }
}