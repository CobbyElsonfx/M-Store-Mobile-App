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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B[\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u000bH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J_\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\"\u001a\u00020\t2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012\u00a8\u0006\'"}, d2 = {"Lcom/momokotle/agent/presentation/contacts/ContactsUiState;", "", "searchQuery", "", "allContacts", "", "Lcom/momokotle/agent/models/SavedContact;", "filteredContacts", "showAddDialog", "", "dialogForm", "Lcom/momokotle/agent/ui/components/SaveContactFormState;", "dialogError", "snackMessage", "(Ljava/lang/String;Ljava/util/List;Ljava/util/List;ZLcom/momokotle/agent/ui/components/SaveContactFormState;Ljava/lang/String;Ljava/lang/String;)V", "getAllContacts", "()Ljava/util/List;", "getDialogError", "()Ljava/lang/String;", "getDialogForm", "()Lcom/momokotle/agent/ui/components/SaveContactFormState;", "getFilteredContacts", "getSearchQuery", "getShowAddDialog", "()Z", "getSnackMessage", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ContactsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.SavedContact> allContacts = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.SavedContact> filteredContacts = null;
    private final boolean showAddDialog = false;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.ui.components.SaveContactFormState dialogForm = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String dialogError = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String snackMessage = null;
    
    public ContactsUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.SavedContact> allContacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.SavedContact> filteredContacts, boolean showAddDialog, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ui.components.SaveContactFormState dialogForm, @org.jetbrains.annotations.Nullable()
    java.lang.String dialogError, @org.jetbrains.annotations.Nullable()
    java.lang.String snackMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.SavedContact> getAllContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.SavedContact> getFilteredContacts() {
        return null;
    }
    
    public final boolean getShowAddDialog() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ui.components.SaveContactFormState getDialogForm() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDialogError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSnackMessage() {
        return null;
    }
    
    public ContactsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.SavedContact> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.SavedContact> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ui.components.SaveContactFormState component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.contacts.ContactsUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.SavedContact> allContacts, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.SavedContact> filteredContacts, boolean showAddDialog, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ui.components.SaveContactFormState dialogForm, @org.jetbrains.annotations.Nullable()
    java.lang.String dialogError, @org.jetbrains.annotations.Nullable()
    java.lang.String snackMessage) {
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