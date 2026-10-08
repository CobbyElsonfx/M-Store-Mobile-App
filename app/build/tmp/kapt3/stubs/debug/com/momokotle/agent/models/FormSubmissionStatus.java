package com.momokotle.agent.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u0000 \u000b2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u000bB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\f"}, d2 = {"Lcom/momokotle/agent/models/FormSubmissionStatus;", "", "firestoreValue", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getFirestoreValue", "()Ljava/lang/String;", "PENDING", "APPROVED", "REJECTED", "RESUBMISSION_REQUIRED", "Companion", "app_debug"})
public enum FormSubmissionStatus {
    /*public static final*/ PENDING /* = new PENDING(null) */,
    /*public static final*/ APPROVED /* = new APPROVED(null) */,
    /*public static final*/ REJECTED /* = new REJECTED(null) */,
    /*public static final*/ RESUBMISSION_REQUIRED /* = new RESUBMISSION_REQUIRED(null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String firestoreValue = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.FormSubmissionStatus.Companion Companion = null;
    
    FormSubmissionStatus(java.lang.String firestoreValue) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFirestoreValue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.momokotle.agent.models.FormSubmissionStatus> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/momokotle/agent/models/FormSubmissionStatus$Companion;", "", "()V", "fromFirestore", "Lcom/momokotle/agent/models/FormSubmissionStatus;", "value", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.momokotle.agent.models.FormSubmissionStatus fromFirestore(@org.jetbrains.annotations.Nullable()
        java.lang.String value) {
            return null;
        }
    }
}