package com.momokotle.agent.presentation.registration;

import android.net.Uri;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.form.AgentFormDefinitions;
import com.momokotle.agent.data.form.FormTextField;
import com.momokotle.agent.data.form.FormUploadSlot;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase;
import com.momokotle.agent.models.AgentFormType;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import com.momokotle.agent.utils.FileDocumentHelper;
import com.momokotle.agent.utils.FormValidators;
import com.momokotle.agent.utils.InternetMessages;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u000fH\u0002J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u0018H\u0002J\u0016\u0010\u001f\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010 \u001a\u00020\u0018J\u000e\u0010!\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u0018J\u0016\u0010\"\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\u0018J\u000e\u0010$\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u0018J\u0016\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u0018J\u0010\u0010+\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u000fH\u0002J\u000e\u0010,\u001a\u00020\u001b2\u0006\u0010-\u001a\u00020\u0018J \u0010.\u001a\u00020/2\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u00100\u001a\u00020\u0018H\u0002J\"\u00101\u001a\u00020/2\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u00182\b\u00102\u001a\u0004\u0018\u00010\u0018H\u0002J\u0006\u00103\u001a\u00020\u001bJ&\u00104\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\'2\u0006\u00105\u001a\u00020\u00182\u0006\u00106\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u00107R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u00068"}, d2 = {"Lcom/momokotle/agent/presentation/registration/AgentFormViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "formSubmissionRepository", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "getAuthenticatedUserUseCase", "Lcom/momokotle/agent/domain/usecase/GetAuthenticatedUserUseCase;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "fileDocumentHelper", "Lcom/momokotle/agent/utils/FileDocumentHelper;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;Lcom/momokotle/agent/domain/usecase/GetAuthenticatedUserUseCase;Lcom/momokotle/agent/utils/NetworkMonitor;Lcom/momokotle/agent/utils/FileDocumentHelper;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/registration/AgentFormUiState;", "formType", "Lcom/momokotle/agent/models/AgentFormType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buildNormalizedFieldValues", "", "", "state", "clearMessages", "", "findField", "Lcom/momokotle/agent/data/form/FormTextField;", "key", "onDateSelected", "formattedDate", "onFieldBlur", "onFieldChange", "raw", "onFieldTouched", "onFileSelected", "slot", "Lcom/momokotle/agent/data/form/FormUploadSlot;", "uri", "Landroid/net/Uri;", "onUploadTouched", "recompute", "removeUpload", "slotKey", "shouldShowFieldValidation", "", "value", "shouldShowUploadValidation", "path", "submit", "uploadToFirebase", "localPath", "submissionId", "(Lcom/momokotle/agent/data/form/FormUploadSlot;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AgentFormViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase getAuthenticatedUserUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.FileDocumentHelper fileDocumentHelper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.AgentFormType formType = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.registration.AgentFormUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.registration.AgentFormUiState> uiState = null;
    
    @javax.inject.Inject()
    public AgentFormViewModel(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.FileDocumentHelper fileDocumentHelper) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.registration.AgentFormUiState> getUiState() {
        return null;
    }
    
    public final void onFieldChange(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
    }
    
    public final void onFieldBlur(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void onDateSelected(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String formattedDate) {
    }
    
    public final void onFieldTouched(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void onUploadTouched(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void onFileSelected(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    private final java.lang.Object uploadToFirebase(com.momokotle.agent.data.form.FormUploadSlot slot, java.lang.String localPath, java.lang.String submissionId, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void removeUpload(@org.jetbrains.annotations.NotNull()
    java.lang.String slotKey) {
    }
    
    public final void submit() {
    }
    
    public final void clearMessages() {
    }
    
    private final com.momokotle.agent.data.form.FormTextField findField(java.lang.String key) {
        return null;
    }
    
    private final java.util.Map<java.lang.String, java.lang.String> buildNormalizedFieldValues(com.momokotle.agent.presentation.registration.AgentFormUiState state) {
        return null;
    }
    
    private final com.momokotle.agent.presentation.registration.AgentFormUiState recompute(com.momokotle.agent.presentation.registration.AgentFormUiState state) {
        return null;
    }
    
    private final boolean shouldShowFieldValidation(com.momokotle.agent.presentation.registration.AgentFormUiState state, java.lang.String key, java.lang.String value) {
        return false;
    }
    
    private final boolean shouldShowUploadValidation(com.momokotle.agent.presentation.registration.AgentFormUiState state, java.lang.String key, java.lang.String path) {
        return false;
    }
}