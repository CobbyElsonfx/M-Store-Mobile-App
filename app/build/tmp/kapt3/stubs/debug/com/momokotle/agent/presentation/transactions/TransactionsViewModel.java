package com.momokotle.agent.presentation.transactions;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.models.TransactionType;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0007R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/momokotle/agent/presentation/transactions/TransactionsViewModel;", "Landroidx/lifecycle/ViewModel;", "transactionRepository", "Lcom/momokotle/agent/domain/repository/TransactionRepository;", "(Lcom/momokotle/agent/domain/repository/TransactionRepository;)V", "filter", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/models/TransactionType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/presentation/transactions/TransactionsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "setFilter", "", "type", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TransactionsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.models.TransactionType> filter = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.transactions.TransactionsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.presentation.transactions.TransactionsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public TransactionsViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.TransactionRepository transactionRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.transactions.TransactionsUiState> getUiState() {
        return null;
    }
    
    public final void setFilter(@org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.TransactionType type) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f\u00a8\u0006\r"}, d2 = {"Lcom/momokotle/agent/presentation/transactions/TransactionsViewModel$Companion;", "", "()V", "formatDate", "", "timestamp", "", "statusLabel", "status", "Lcom/momokotle/agent/models/TransactionStatus;", "typeLabel", "type", "Lcom/momokotle/agent/models/TransactionType;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String formatDate(long timestamp) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String typeLabel(@org.jetbrains.annotations.NotNull()
        com.momokotle.agent.models.TransactionType type) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String statusLabel(@org.jetbrains.annotations.NotNull()
        com.momokotle.agent.models.TransactionStatus status) {
            return null;
        }
    }
}