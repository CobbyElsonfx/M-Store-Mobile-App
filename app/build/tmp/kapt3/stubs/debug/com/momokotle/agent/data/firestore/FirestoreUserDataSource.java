package com.momokotle.agent.data.firestore;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.momokotle.agent.models.UserProfile;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreUserDataSource;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "createUserProfile", "", "profile", "Lcom/momokotle/agent/models/UserProfile;", "(Lcom/momokotle/agent/models/UserProfile;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUserProfile", "userId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "usersCollection", "Lcom/google/firebase/firestore/CollectionReference;", "Companion", "app_debug"})
public final class FirestoreUserDataSource {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COLLECTION_USERS = "users";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_USER_ID = "userId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_FULL_NAME = "fullName";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_EMAIL = "email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PHONE_NUMBER = "phoneNumber";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_LOCATION = "location";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_ACCOUNT_STATUS = "accountStatus";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_CREATED_AT = "createdAt";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_UPDATED_AT = "updatedAt";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.data.firestore.FirestoreUserDataSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public FirestoreUserDataSource(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    private final com.google.firebase.firestore.CollectionReference usersCollection() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createUserProfile(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.UserProfile profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchUserProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.UserProfile> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreUserDataSource$Companion;", "", "()V", "COLLECTION_USERS", "", "FIELD_ACCOUNT_STATUS", "FIELD_CREATED_AT", "FIELD_EMAIL", "FIELD_FULL_NAME", "FIELD_LOCATION", "FIELD_PHONE_NUMBER", "FIELD_UPDATED_AT", "FIELD_USER_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}