package com.momokotle.agent.presentation.transactions;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.ui.components.AppStatusTone;
import com.momokotle.agent.ui.theme.AppDimens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0014\u0010\u0004\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0003\u001a.\u0010\t\u001a\u00020\u00012\u0010\b\u0002\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a8\u0006\u0010"}, d2 = {"FilterRow", "", "selected", "Lcom/momokotle/agent/models/TransactionType;", "onSelect", "Lkotlin/Function1;", "TransactionCard", "transaction", "Lcom/momokotle/agent/models/Transaction;", "TransactionsScreen", "onBack", "Lkotlin/Function0;", "isRootTab", "", "viewModel", "Lcom/momokotle/agent/presentation/transactions/TransactionsViewModel;", "app_debug"})
public final class TransactionsScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void TransactionsScreen(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, boolean isRootTab, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.transactions.TransactionsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FilterRow(com.momokotle.agent.models.TransactionType selected, kotlin.jvm.functions.Function1<? super com.momokotle.agent.models.TransactionType, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TransactionCard(com.momokotle.agent.models.Transaction transaction) {
    }
}