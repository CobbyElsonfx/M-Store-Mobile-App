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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b&\b\u0087\b\u0018\u00002\u00020\u0001B\u008b\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u000f\u001a\u00020\f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\u0014J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010(\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\u0015\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bH\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\t\u0010+\u001a\u00020\fH\u00c6\u0003J\t\u0010,\u001a\u00020\fH\u00c6\u0003J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\u008f\u0001\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u000f\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\f2\b\b\u0002\u0010\u0011\u001a\u00020\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010/\u001a\u00020\u00032\b\u00100\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00101\u001a\u00020\rH\u00d6\u0001J\t\u00102\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u0010\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000f\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001bR\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001bR\u0013\u0010\u0012\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u001d\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"\u00a8\u00063"}, d2 = {"Lcom/momokotle/agent/presentation/store/StoreUiState;", "", "isOnline", "", "isLoadingProducts", "selectedCategory", "Lcom/momokotle/agent/models/StoreCategory;", "products", "", "Lcom/momokotle/agent/models/StoreProduct;", "quantities", "", "", "", "checkoutProduct", "checkoutPhone", "checkoutLocation", "isSubmitting", "message", "errorMessage", "(ZZLcom/momokotle/agent/models/StoreCategory;Ljava/util/List;Ljava/util/Map;Lcom/momokotle/agent/models/StoreProduct;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;)V", "getCheckoutLocation", "()Ljava/lang/String;", "getCheckoutPhone", "getCheckoutProduct", "()Lcom/momokotle/agent/models/StoreProduct;", "getErrorMessage", "()Z", "getMessage", "getProducts", "()Ljava/util/List;", "getQuantities", "()Ljava/util/Map;", "getSelectedCategory", "()Lcom/momokotle/agent/models/StoreCategory;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class StoreUiState {
    private final boolean isOnline = false;
    private final boolean isLoadingProducts = false;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.StoreCategory selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.StoreProduct> products = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Integer> quantities = null;
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.StoreProduct checkoutProduct = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String checkoutPhone = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String checkoutLocation = null;
    private final boolean isSubmitting = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String message = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public StoreUiState(boolean isOnline, boolean isLoadingProducts, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.StoreProduct> products, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> quantities, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.StoreProduct checkoutProduct, @org.jetbrains.annotations.NotNull()
    java.lang.String checkoutPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String checkoutLocation, boolean isSubmitting, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean isOnline() {
        return false;
    }
    
    public final boolean isLoadingProducts() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.StoreCategory getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.StoreProduct> getProducts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getQuantities() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.StoreProduct getCheckoutProduct() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCheckoutPhone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCheckoutLocation() {
        return null;
    }
    
    public final boolean isSubmitting() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public StoreUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.StoreCategory component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.StoreProduct> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.StoreProduct component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.store.StoreUiState copy(boolean isOnline, boolean isLoadingProducts, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.StoreProduct> products, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> quantities, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.StoreProduct checkoutProduct, @org.jetbrains.annotations.NotNull()
    java.lang.String checkoutPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String checkoutLocation, boolean isSubmitting, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
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