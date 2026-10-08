package com.momokotle.agent.domain.repository;

import com.momokotle.agent.models.FormSubmission;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0004H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\f\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\tH&J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0011\u001a\u00020\tH&J\b\u0010\u0014\u001a\u00020\u0013H&JR\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\t2\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00130\u0019H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001b\u0010\u001c\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001d"}, d2 = {"Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "", "createAndSubmitForm", "Lkotlin/Result;", "Lcom/momokotle/agent/models/FormSubmission;", "submission", "createAndSubmitForm-gIAlu-s", "(Lcom/momokotle/agent/models/FormSubmission;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateSubmissionId", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSubmission", "submissionId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeMySubmissions", "Lkotlinx/coroutines/flow/Flow;", "", "userId", "startSync", "", "stopSync", "uploadDocumentSlot", "slotId", "localPath", "onProgress", "Lkotlin/Function1;", "", "uploadDocumentSlot-hUnOzRk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface FormSubmissionRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object generateSubmissionId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.FormSubmission>> observeMySubmissions(@org.jetbrains.annotations.NotNull()
    java.lang.String userId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSubmission(@org.jetbrains.annotations.NotNull()
    java.lang.String submissionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.FormSubmission> $completion);
    
    public abstract void startSync(@org.jetbrains.annotations.NotNull()
    java.lang.String userId);
    
    public abstract void stopSync();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}