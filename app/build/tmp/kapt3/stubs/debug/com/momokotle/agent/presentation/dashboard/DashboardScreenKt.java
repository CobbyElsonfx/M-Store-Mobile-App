package com.momokotle.agent.presentation.dashboard;

import android.Manifest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.lazy.grid.GridCells;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.lifecycle.SavedStateHandle;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.momokotle.agent.models.AutomationStatus;
import com.momokotle.agent.models.ContactNetwork;
import com.momokotle.agent.models.Transaction;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a*\u0010\b\u001a\u00020\u00012\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006\u000e"}, d2 = {"DashboardScreen", "", "onCustomMenu", "Lkotlin/Function0;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "viewModel", "Lcom/momokotle/agent/presentation/dashboard/DashboardViewModel;", "LastTransactionCard", "transaction", "Lcom/momokotle/agent/models/Transaction;", "typeLabel", "", "onRepeat", "app_debug"})
public final class DashboardScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCustomMenu, @org.jetbrains.annotations.Nullable()
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.dashboard.DashboardViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LastTransactionCard(com.momokotle.agent.models.Transaction transaction, java.lang.String typeLabel, kotlin.jvm.functions.Function0<kotlin.Unit> onRepeat) {
    }
}