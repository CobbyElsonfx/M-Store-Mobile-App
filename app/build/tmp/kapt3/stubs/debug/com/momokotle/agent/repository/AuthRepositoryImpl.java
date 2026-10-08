package com.momokotle.agent.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.data.firestore.FirestoreUserDataSource;
import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.AuthResult;
import com.momokotle.agent.models.UserProfile;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 (2\u00020\u0001:\u0001(B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nH\u0002J\u0010\u0010\f\u001a\u0004\u0018\u00010\rH\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u0011\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0012\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0013J\u001e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u000e\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u001b\u001a\u00020\u00102\n\u0010\u001c\u001a\u00060\u001dj\u0002`\u001eH\u0002J\u0010\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\nH\u0016J\u0010\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\nH\u0016J\u000e\u0010!\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u000eJ6\u0010\"\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010&J\u000e\u0010\'\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/momokotle/agent/repository/AuthRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/AuthRepository;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firestoreUserDataSource", "Lcom/momokotle/agent/data/firestore/FirestoreUserDataSource;", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "(Lcom/google/firebase/auth/FirebaseAuth;Lcom/momokotle/agent/data/firestore/FirestoreUserDataSource;Lcom/momokotle/agent/data/datastore/UserAccountDataStore;)V", "firebaseAuthStateFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/google/firebase/auth/FirebaseUser;", "getCurrentUser", "Lcom/momokotle/agent/models/UserProfile;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentUserId", "", "loadProfileForFirebaseUser", "firebaseUser", "(Lcom/google/firebase/auth/FirebaseUser;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "Lcom/momokotle/agent/domain/repository/AuthResult;", "email", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "", "mapFirebaseError", "error", "Ljava/lang/Exception;", "Lkotlin/Exception;", "observeAuthState", "observeCachedProfile", "refreshUserProfile", "register", "fullName", "phoneNumber", "location", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requireAuthenticatedProfile", "Companion", "app_debug"})
public final class AuthRepositoryImpl implements com.momokotle.agent.domain.repository.AuthRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth firebaseAuth = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.FirestoreUserDataSource firestoreUserDataSource = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SUSPENDED_MESSAGE = "Your account has been suspended. Please contact support.";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.repository.AuthRepositoryImpl.Companion Companion = null;
    
    @javax.inject.Inject()
    public AuthRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth firebaseAuth, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.FirestoreUserDataSource firestoreUserDataSource, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.momokotle.agent.models.UserProfile> observeAuthState() {
        return null;
    }
    
    private final kotlinx.coroutines.flow.Flow<com.google.firebase.auth.FirebaseUser> firebaseAuthStateFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.momokotle.agent.models.UserProfile> observeCachedProfile() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object register(@org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String location, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.repository.AuthResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.repository.AuthResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCurrentUser(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.UserProfile> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCurrentUserId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object refreshUserProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.repository.AuthResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object requireAuthenticatedProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.UserProfile> $completion) {
        return null;
    }
    
    private final java.lang.Object loadProfileForFirebaseUser(com.google.firebase.auth.FirebaseUser firebaseUser, kotlin.coroutines.Continuation<? super com.momokotle.agent.models.UserProfile> $completion) {
        return null;
    }
    
    private final java.lang.String mapFirebaseError(java.lang.Exception error) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/momokotle/agent/repository/AuthRepositoryImpl$Companion;", "", "()V", "SUSPENDED_MESSAGE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}