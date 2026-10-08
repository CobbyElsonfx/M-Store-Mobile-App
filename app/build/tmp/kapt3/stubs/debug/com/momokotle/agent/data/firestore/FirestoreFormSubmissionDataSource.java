package com.momokotle.agent.data.firestore;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.momokotle.agent.data.form.DocumentSlotRegistry;
import com.momokotle.agent.models.AgentFormType;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eJ(\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreFormSubmissionDataSource;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "createSubmission", "", "submission", "Lcom/momokotle/agent/models/FormSubmission;", "(Lcom/momokotle/agent/models/FormSubmission;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeSubmissionsForUser", "Lkotlinx/coroutines/flow/Flow;", "", "userId", "", "updateSubmissionStatus", "submissionId", "status", "Lcom/momokotle/agent/models/FormSubmissionStatus;", "adminNote", "(Ljava/lang/String;Lcom/momokotle/agent/models/FormSubmissionStatus;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class FirestoreFormSubmissionDataSource {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COLLECTION_FORM_SUBMISSIONS = "form_submissions";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LEGACY_COLLECTION = "formSubmissions";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_SUBMISSION_ID = "submissionId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_USER_ID = "userId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_FORM_TYPE = "formType";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_APPLICANT_NAME = "applicantName";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PHONE_NUMBER = "phoneNumber";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_FORM_DATA = "formData";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_DOCUMENTS = "documents";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_UPLOADED_DOCUMENTS = "uploadedDocuments";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_STATUS = "status";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_ADMIN_NOTE = "adminNote";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_CREATED_AT = "createdAt";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_UPDATED_AT = "updatedAt";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.data.firestore.FirestoreFormSubmissionDataSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public FirestoreFormSubmissionDataSource(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createSubmission(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.FormSubmission submission, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSubmissionStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String submissionId, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.FormSubmissionStatus status, @org.jetbrains.annotations.Nullable()
    java.lang.String adminNote, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.FormSubmission>> observeSubmissionsForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreFormSubmissionDataSource$Companion;", "", "()V", "COLLECTION_FORM_SUBMISSIONS", "", "FIELD_ADMIN_NOTE", "FIELD_APPLICANT_NAME", "FIELD_CREATED_AT", "FIELD_DOCUMENTS", "FIELD_FORM_DATA", "FIELD_FORM_TYPE", "FIELD_PHONE_NUMBER", "FIELD_STATUS", "FIELD_SUBMISSION_ID", "FIELD_UPDATED_AT", "FIELD_UPLOADED_DOCUMENTS", "FIELD_USER_ID", "LEGACY_COLLECTION", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}