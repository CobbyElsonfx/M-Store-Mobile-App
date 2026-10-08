package com.momokotle.agent.repository;

import com.momokotle.agent.data.firestore.FirestoreProductDataSource;
import com.momokotle.agent.di.ApplicationScope;
import com.momokotle.agent.database.dao.StoreProductDao;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.repository.ProductValidationResult;
import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreProduct;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0082@\u00a2\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u0016H\u0016J\u001c\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010\u001b\u001a\u00020\fH\u0016J\u001e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u001fH\u0096@\u00a2\u0006\u0002\u0010 R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/momokotle/agent/repository/ProductRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/ProductRepository;", "storeProductDao", "Lcom/momokotle/agent/database/dao/StoreProductDao;", "firestoreProductDataSource", "Lcom/momokotle/agent/data/firestore/FirestoreProductDataSource;", "applicationScope", "Lkotlinx/coroutines/CoroutineScope;", "(Lcom/momokotle/agent/database/dao/StoreProductDao;Lcom/momokotle/agent/data/firestore/FirestoreProductDataSource;Lkotlinx/coroutines/CoroutineScope;)V", "syncJob", "Lkotlinx/coroutines/Job;", "cacheProducts", "", "products", "", "Lcom/momokotle/agent/models/StoreProduct;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProduct", "productId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeProducts", "Lkotlinx/coroutines/flow/Flow;", "observeProductsByCategory", "category", "Lcom/momokotle/agent/models/StoreCategory;", "startSync", "stopSync", "validateForPurchase", "Lcom/momokotle/agent/domain/repository/ProductValidationResult;", "quantity", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ProductRepositoryImpl implements com.momokotle.agent.domain.repository.ProductRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.StoreProductDao storeProductDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.FirestoreProductDataSource firestoreProductDataSource = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope applicationScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job syncJob;
    
    @javax.inject.Inject()
    public ProductRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.StoreProductDao storeProductDao, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.FirestoreProductDataSource firestoreProductDataSource, @com.momokotle.agent.di.ApplicationScope()
    @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope applicationScope) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreProduct>> observeProducts() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreProduct>> observeProductsByCategory(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreCategory category) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getProduct(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.StoreProduct> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object validateForPurchase(@org.jetbrains.annotations.NotNull()
    java.lang.String productId, int quantity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.repository.ProductValidationResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public void startSync() {
    }
    
    @java.lang.Override()
    public void stopSync() {
    }
    
    private final java.lang.Object cacheProducts(java.util.List<com.momokotle.agent.models.StoreProduct> products, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}