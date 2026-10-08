package com.momokotle.agent.domain.repository;

import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreProduct;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\t0\bH&J\u001c\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\t0\b2\u0006\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u000eH&J\u001e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/momokotle/agent/domain/repository/ProductRepository;", "", "getProduct", "Lcom/momokotle/agent/models/StoreProduct;", "productId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeProducts", "Lkotlinx/coroutines/flow/Flow;", "", "observeProductsByCategory", "category", "Lcom/momokotle/agent/models/StoreCategory;", "startSync", "", "stopSync", "validateForPurchase", "Lcom/momokotle/agent/domain/repository/ProductValidationResult;", "quantity", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ProductRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreProduct>> observeProducts();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreProduct>> observeProductsByCategory(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory category);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProduct(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.StoreProduct> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object validateForPurchase(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, int quantity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.repository.ProductValidationResult> $completion);
    
    public abstract void startSync();
    
    public abstract void stopSync();
}