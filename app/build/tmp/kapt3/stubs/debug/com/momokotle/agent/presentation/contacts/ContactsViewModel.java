package com.momokotle.agent.presentation.contacts;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.usecase.SaveContactResult;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
import com.momokotle.agent.models.ContactNetwork;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.ui.components.SaveContactFormState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u000fJ$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0019J\u000e\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0019J\u000e\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0019J\u0006\u0010$\u001a\u00020\u000fJ\u0006\u0010%\u001a\u00020\u000fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006&"}, d2 = {"Lcom/momokotle/agent/presentation/contacts/ContactsViewModel;", "Landroidx/lifecycle/ViewModel;", "savedContactRepository", "Lcom/momokotle/agent/domain/repository/SavedContactRepository;", "saveContactUseCase", "Lcom/momokotle/agent/domain/usecase/SaveContactUseCase;", "(Lcom/momokotle/agent/domain/repository/SavedContactRepository;Lcom/momokotle/agent/domain/usecase/SaveContactUseCase;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/contacts/ContactsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearSnackMessage", "", "deleteContact", "id", "", "dismissDialog", "filterContacts", "", "Lcom/momokotle/agent/models/SavedContact;", "contacts", "query", "", "onDialogNameChange", "value", "onDialogNetworkChange", "network", "Lcom/momokotle/agent/models/ContactNetwork;", "onDialogPhoneChange", "onDialogPortedChange", "checked", "", "onSearchChange", "openAddDialog", "saveFromDialog", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ContactsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.SaveContactUseCase saveContactUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.contacts.ContactsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.contacts.ContactsUiState> uiState = null;
    
    @javax.inject.Inject()
    public ContactsViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.SaveContactUseCase saveContactUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.contacts.ContactsUiState> getUiState() {
        return null;
    }
    
    public final void onSearchChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    private final java.util.List<com.momokotle.agent.models.SavedContact> filterContacts(java.util.List<com.momokotle.agent.models.SavedContact> contacts, java.lang.String query) {
        return null;
    }
    
    public final void openAddDialog() {
    }
    
    public final void dismissDialog() {
    }
    
    public final void onDialogNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onDialogPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onDialogNetworkChange(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network) {
    }
    
    public final void onDialogPortedChange(boolean checked) {
    }
    
    public final void saveFromDialog() {
    }
    
    public final void deleteContact(long id) {
    }
    
    public final void clearSnackMessage() {
    }
}