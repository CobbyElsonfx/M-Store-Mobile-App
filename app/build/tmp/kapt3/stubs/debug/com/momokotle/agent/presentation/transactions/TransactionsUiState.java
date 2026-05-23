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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B!\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J%\u0010\u000e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/momokotle/agent/presentation/transactions/TransactionsUiState;", "", "transactions", "", "Lcom/momokotle/agent/models/Transaction;", "filter", "Lcom/momokotle/agent/models/TransactionType;", "(Ljava/util/List;Lcom/momokotle/agent/models/TransactionType;)V", "getFilter", "()Lcom/momokotle/agent/models/TransactionType;", "getTransactions", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class TransactionsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.Transaction> transactions = null;
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.TransactionType filter = null;
    
    public TransactionsUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.Transaction> transactions, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.TransactionType filter) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.Transaction> getTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.TransactionType getFilter() {
        return null;
    }
    
    public TransactionsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.Transaction> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.TransactionType component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.transactions.TransactionsUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.Transaction> transactions, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.TransactionType filter) {
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