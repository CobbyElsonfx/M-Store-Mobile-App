package com.momokotle.agent.data.form;

import java.io.File;
import java.util.Locale;

/**
 * Maps form upload slot keys to Firebase Storage filenames and Firestore document URL fields.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\f\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J&\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004J\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0005J&\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004J\u0016\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/momokotle/agent/data/form/DocumentSlotRegistry;", "", "()V", "byFirestoreKey", "", "", "Lcom/momokotle/agent/data/form/DocumentSlotRegistry$SlotMapping;", "bySlotKey", "mappings", "", "documentDisplayName", "firestoreUrlKey", "firestoreDocumentsToSlotUrls", "documents", "mappingFor", "slotKey", "slotUrlsToFirestoreDocuments", "slotUrls", "storageFileName", "localPath", "SlotMapping", "app_debug"})
public final class DocumentSlotRegistry {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.momokotle.agent.data.form.DocumentSlotRegistry.SlotMapping> mappings = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, com.momokotle.agent.data.form.DocumentSlotRegistry.SlotMapping> bySlotKey = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, com.momokotle.agent.data.form.DocumentSlotRegistry.SlotMapping> byFirestoreKey = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.data.form.DocumentSlotRegistry INSTANCE = null;
    
    private DocumentSlotRegistry() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.data.form.DocumentSlotRegistry.SlotMapping mappingFor(@org.jetbrains.annotations.NotNull()
    java.lang.String slotKey) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String storageFileName(@org.jetbrains.annotations.NotNull()
    java.lang.String slotKey, @org.jetbrains.annotations.NotNull()
    java.lang.String localPath) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> slotUrlsToFirestoreDocuments(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> slotUrls) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> firestoreDocumentsToSlotUrls(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> documents) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String documentDisplayName(@org.jetbrains.annotations.NotNull()
    java.lang.String firestoreUrlKey) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/momokotle/agent/data/form/DocumentSlotRegistry$SlotMapping;", "", "slotKey", "", "storageFileName", "firestoreUrlKey", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getFirestoreUrlKey", "()Ljava/lang/String;", "getSlotKey", "getStorageFileName", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class SlotMapping {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String slotKey = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String storageFileName = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String firestoreUrlKey = null;
        
        public SlotMapping(@org.jetbrains.annotations.NotNull()
        java.lang.String slotKey, @org.jetbrains.annotations.NotNull()
        java.lang.String storageFileName, @org.jetbrains.annotations.NotNull()
        java.lang.String firestoreUrlKey) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSlotKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getStorageFileName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getFirestoreUrlKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.momokotle.agent.data.form.DocumentSlotRegistry.SlotMapping copy(@org.jetbrains.annotations.NotNull()
        java.lang.String slotKey, @org.jetbrains.annotations.NotNull()
        java.lang.String storageFileName, @org.jetbrains.annotations.NotNull()
        java.lang.String firestoreUrlKey) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}