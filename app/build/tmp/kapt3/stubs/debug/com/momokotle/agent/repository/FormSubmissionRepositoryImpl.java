package com.momokotle.agent.repository;

import com.momokotle.agent.data.firestore.FirebaseSubmissionStorage;
import com.momokotle.agent.data.firestore.FirestoreFormSubmissionDataSource;
import com.momokotle.agent.data.firestore.SubmissionIdGenerator;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.di.ApplicationScope;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import com.momokotle.agent.utils.NetworkMonitor;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B9\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ$\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00122\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0082@\u00a2\u0006\u0002\u0010\u0019J$\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00180\u001b2\u0006\u0010\u001c\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010 J\u0018\u0010!\u001a\u0004\u0018\u00010\u00182\u0006\u0010\"\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010#J\u001c\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00170%2\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\u001c\u0010&\u001a\u00020\u00122\u0012\u0010\'\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00120(H\u0002J\u0016\u0010)\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010\u001eJ\u0010\u0010*\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\b\u0010+\u001a\u00020\u0014H\u0016JP\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00120\u001b2\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020\u00122\u0006\u0010-\u001a\u00020\u00122\u0006\u0010.\u001a\u00020\u00122\u0012\u0010/\u001a\u000e\u0012\u0004\u0012\u000201\u0012\u0004\u0012\u00020\u001400H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u00103R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00064"}, d2 = {"Lcom/momokotle/agent/repository/FormSubmissionRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "formSubmissionDao", "Lcom/momokotle/agent/database/dao/FormSubmissionDao;", "firestoreDataSource", "Lcom/momokotle/agent/data/firestore/FirestoreFormSubmissionDataSource;", "submissionStorage", "Lcom/momokotle/agent/data/firestore/FirebaseSubmissionStorage;", "submissionIdGenerator", "Lcom/momokotle/agent/data/firestore/SubmissionIdGenerator;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "applicationScope", "Lkotlinx/coroutines/CoroutineScope;", "(Lcom/momokotle/agent/database/dao/FormSubmissionDao;Lcom/momokotle/agent/data/firestore/FirestoreFormSubmissionDataSource;Lcom/momokotle/agent/data/firestore/FirebaseSubmissionStorage;Lcom/momokotle/agent/data/firestore/SubmissionIdGenerator;Lcom/momokotle/agent/utils/NetworkMonitor;Lkotlinx/coroutines/CoroutineScope;)V", "syncJob", "Lkotlinx/coroutines/Job;", "syncedUserId", "", "cacheSubmissionsForUser", "", "userId", "submissions", "", "Lcom/momokotle/agent/models/FormSubmission;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createAndSubmitForm", "Lkotlin/Result;", "submission", "createAndSubmitForm-gIAlu-s", "(Lcom/momokotle/agent/models/FormSubmission;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateSubmissionId", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSubmission", "submissionId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeMySubmissions", "Lkotlinx/coroutines/flow/Flow;", "resolveApplicantName", "formData", "", "rollbackUploadedDocuments", "startSync", "stopSync", "uploadDocumentSlot", "slotId", "localPath", "onProgress", "Lkotlin/Function1;", "", "uploadDocumentSlot-hUnOzRk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FormSubmissionRepositoryImpl implements com.momokotle.agent.domain.repository.FormSubmissionRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.FormSubmissionDao formSubmissionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.FirestoreFormSubmissionDataSource firestoreDataSource = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.FirebaseSubmissionStorage submissionStorage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.SubmissionIdGenerator submissionIdGenerator = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope applicationScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job syncJob;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String syncedUserId;
    
    @javax.inject.Inject()
    public FormSubmissionRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.FormSubmissionDao formSubmissionDao, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.FirestoreFormSubmissionDataSource firestoreDataSource, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.FirebaseSubmissionStorage submissionStorage, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.SubmissionIdGenerator submissionIdGenerator, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor, @com.momokotle.agent.di.ApplicationScope()
    @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope applicationScope) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object generateSubmissionId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.FormSubmission>> observeMySubmissions(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSubmission(@org.jetbrains.annotations.NotNull()
    java.lang.String submissionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.FormSubmission> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public void startSync(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    @java.lang.Override()
    public void stopSync() {
    }
    
    private final java.lang.Object cacheSubmissionsForUser(java.lang.String userId, java.util.List<com.momokotle.agent.models.FormSubmission> submissions, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object rollbackUploadedDocuments(com.momokotle.agent.models.FormSubmission submission, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String resolveApplicantName(java.util.Map<java.lang.String, java.lang.String> formData) {
        return null;
    }
}