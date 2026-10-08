package com.momokotle.agent.presentation.store;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.repository.ProductValidationResult;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase;
import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreProduct;
import com.momokotle.agent.utils.InternetMessages;
import com.momokotle.agent.utils.NetworkMonitor;
import com.momokotle.agent.utils.PhoneNumberFormatter;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u0010\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001e\u001a\u00020\u0015H\u0014J\u000e\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u0015J\u000e\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001cJ\u0006\u0010&\u001a\u00020\u0015J\u000e\u0010\'\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019J\u0016\u0010(\u001a\u00020\u00152\u0006\u0010%\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020$R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006*"}, d2 = {"Lcom/momokotle/agent/presentation/store/StoreViewModel;", "Landroidx/lifecycle/ViewModel;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "productRepository", "Lcom/momokotle/agent/domain/repository/ProductRepository;", "storeRepository", "Lcom/momokotle/agent/domain/repository/StoreRepository;", "getAuthenticatedUserUseCase", "Lcom/momokotle/agent/domain/usecase/GetAuthenticatedUserUseCase;", "(Lcom/momokotle/agent/utils/NetworkMonitor;Lcom/momokotle/agent/domain/repository/ProductRepository;Lcom/momokotle/agent/domain/repository/StoreRepository;Lcom/momokotle/agent/domain/usecase/GetAuthenticatedUserUseCase;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/store/StoreUiState;", "productsJob", "Lkotlinx/coroutines/Job;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessages", "", "dismissCheckout", "observeProductsForCategory", "category", "Lcom/momokotle/agent/models/StoreCategory;", "onCheckoutLocationChange", "v", "", "onCheckoutPhoneChange", "onCleared", "openCheckout", "product", "Lcom/momokotle/agent/models/StoreProduct;", "placeOrder", "quantityFor", "", "productId", "refreshConnectivity", "selectCategory", "setQuantity", "quantity", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StoreViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.StoreRepository storeRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase getAuthenticatedUserUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.store.StoreUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.store.StoreUiState> uiState = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job productsJob;
    
    @javax.inject.Inject()
    public StoreViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.ProductRepository productRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.StoreRepository storeRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.store.StoreUiState> getUiState() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public final void refreshConnectivity() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory category) {
    }
    
    private final void observeProductsForCategory(com.momokotle.agent.models.StoreCategory category) {
    }
    
    public final void setQuantity(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, int quantity) {
    }
    
    public final int quantityFor(@org.jetbrains.annotations.NotNull()
    java.lang.String productId) {
        return 0;
    }
    
    public final void openCheckout(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreProduct product) {
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