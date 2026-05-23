package com.momokotle.agent.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/momokotle/agent/utils/InternetMessages;", "", "()V", "FORM_REQUIRES_INTERNET", "", "STORE_REQUIRES_INTERNET", "app_debug"})
public final class InternetMessages {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STORE_REQUIRES_INTERNET = "Internet is required to use the store";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORM_REQUIRES_INTERNET = "Internet is required to submit this form";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.InternetMessages INSTANCE = null;
    
    private InternetMessages() {
        super();
    }
}