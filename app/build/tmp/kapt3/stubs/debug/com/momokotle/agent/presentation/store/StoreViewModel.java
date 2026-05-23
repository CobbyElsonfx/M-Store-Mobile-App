package com.momokotle.agent.presentation.store;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.catalog.StoreCatalog;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreItem;
import com.momokotle.agent.utils.InternetMessages;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0019J\u0006\u0010\u001a\u001a\u00020\u0011J\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0015J\u0006\u0010\u001e\u001a\u00020\u0011J\u000e\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020!J\u0016\u0010\"\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u001cR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/momokotle/agent/presentation/store/StoreViewModel;", "Landroidx/lifecycle/ViewModel;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "storeRepository", "Lcom/momokotle/agent/domain/repository/StoreRepository;", "userIdProvider", "Lcom/momokotle/agent/domain/repository/UserIdProvider;", "(Lcom/momokotle/agent/utils/NetworkMonitor;Lcom/momokotle/agent/domain/repository/StoreRepository;Lcom/momokotle/agent/domain/repository/UserIdProvider;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/store/StoreUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessages", "", "dismissCheckout", "onCheckoutLocationChange", "v", "", "onCheckoutPhoneChange", "openCheckout", "item", "Lcom/momokotle/agent/models/StoreItem;", "placeOrder", "quantityFor", "", "itemId", "refreshConnectivity", "selectCategory", "category", "Lcom/momokotle/agent/models/StoreCategory;", "setQuantity", "quantity", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StoreViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.StoreRepository storeRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.UserIdProvider userIdProvider = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.store.StoreUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.store.StoreUiState> uiState = null;
    
    @javax.inject.Inject()
    public StoreViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.StoreRepository storeRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.UserIdProvider userIdProvider) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.store.StoreUiState> getUiState() {
        return null;
    }
    
    public final void refreshConnectivity() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory category) {
    }
    
    public final void setQuantity(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, int quantity) {
    }
    
    public final int quantityFor(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId) {
        return 0;
    }
    
    public final void openCheckout(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreItem item) {
    }
    
    public final void dismissCheckout() {
    }
    
    public final void onCheckoutPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onCheckoutLocationChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void placeOrder() {
    }
    
    public final void clearMessages() {
    }
}