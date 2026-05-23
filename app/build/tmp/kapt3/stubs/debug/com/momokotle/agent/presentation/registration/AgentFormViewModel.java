package com.momokotle.agent.presentation.registration;

import android.net.Uri;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.form.AgentFormDefinition;
import com.momokotle.agent.data.form.AgentFormDefinitions;
import com.momokotle.agent.data.form.FormUploadSlot;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.models.AgentFormType;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import com.momokotle.agent.utils.FileDocumentHelper;
import com.momokotle.agent.utils.InternetMessages;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aJ\u0016\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u001aJ\u0018\u0010#\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u001aH\u0002J\u0006\u0010%\u001a\u00020\u0017R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/momokotle/agent/presentation/registration/AgentFormViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "formSubmissionRepository", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "userIdProvider", "Lcom/momokotle/agent/domain/repository/UserIdProvider;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "fileDocumentHelper", "Lcom/momokotle/agent/utils/FileDocumentHelper;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;Lcom/momokotle/agent/domain/repository/UserIdProvider;Lcom/momokotle/agent/utils/NetworkMonitor;Lcom/momokotle/agent/utils/FileDocumentHelper;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/registration/AgentFormUiState;", "formType", "Lcom/momokotle/agent/models/AgentFormType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessages", "", "onFieldChange", "key", "", "value", "onFileSelected", "slot", "Lcom/momokotle/agent/data/form/FormUploadSlot;", "uri", "Landroid/net/Uri;", "removeUpload", "slotKey", "setUpload", "localPath", "submit", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AgentFormViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.UserIdProvider userIdProvider = null;
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
    com.momokotle.agent.domain.repository.UserIdProvider userIdProvider, @org.jetbrains.annotations.NotNull()
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
    java.lang.String value) {
    }
    
    public final void onFileSelected(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    private final void setUpload(com.momokotle.agent.data.form.FormUploadSlot slot, java.lang.String localPath) {
    }
    
    public final void removeUpload(@org.jetbrains.annotations.NotNull()
    java.lang.String slotKey) {
    }
    
    public final void submit() {
    }
    
    public final void clearMessages() {
    }
}