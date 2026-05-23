package com.momokotle.agent.utils;

import com.momokotle.agent.models.TransactionStatus;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/momokotle/agent/utils/UssdSecurityHelper;", "", "()V", "securityKeywords", "", "", "containsSensitiveKeyword", "", "text", "app_debug"})
public final class UssdSecurityHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> securityKeywords = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.UssdSecurityHelper INSTANCE = null;
    
    private UssdSecurityHelper() {
        super();
    }
    
    public final boolean containsSensitiveKeyword(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
}