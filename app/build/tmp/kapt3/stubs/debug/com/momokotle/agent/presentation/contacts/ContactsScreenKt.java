package com.momokotle.agent.presentation.contacts;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.models.SavedContact;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a,\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a4\u0010\u0007\u001a\u00020\u00012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u00a8\u0006\r"}, d2 = {"ContactListItem", "", "contact", "Lcom/momokotle/agent/models/SavedContact;", "onUse", "Lkotlin/Function0;", "onDelete", "ContactsScreen", "onBack", "onUseContact", "Lkotlin/Function1;", "viewModel", "Lcom/momokotle/agent/presentation/contacts/ContactsViewModel;", "app_debug"})
public final class ContactsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ContactsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.momokotle.agent.models.SavedContact, kotlin.Unit> onUseContact, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.contacts.ContactsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ContactListItem(com.momokotle.agent.models.SavedContact contact, kotlin.jvm.functions.Function0<kotlin.Unit> onUse, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
}