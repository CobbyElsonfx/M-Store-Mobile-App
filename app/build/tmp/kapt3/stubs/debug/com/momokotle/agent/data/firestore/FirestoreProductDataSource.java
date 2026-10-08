package com.momokotle.agent.data.firestore;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.momokotle.agent.models.StoreProduct;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreProductDataSource;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "observeProductsRealtime", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/momokotle/agent/models/StoreProduct;", "Companion", "app_debug"})
public final class FirestoreProductDataSource {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COLLECTION_STORE_PRODUCTS = "storeProducts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PRODUCT_NAME = "productName";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PRODUCT_DESCRIPTION = "productDescription";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_CATEGORY = "category";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PRICE = "price";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_STOCK_QUANTITY = "stockQuantity";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PRODUCT_IMAGE_URL = "productImageUrl";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_IS_AVAILABLE = "isAvailable";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_CREATED_AT = "createdAt";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_UPDATED_AT = "updatedAt";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.data.firestore.FirestoreProductDataSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public FirestoreProductDataSource(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreProduct>> observeProductsRealtime() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreProductDataSource$Companion;", "", "()V", "COLLECTION_STORE_PRODUCTS", "", "FIELD_CATEGORY", "FIELD_CREATED_AT", "FIELD_IS_AVAILABLE", "FIELD_PRICE", "FIELD_PRODUCT_DESCRIPTION", "FIELD_PRODUCT_IMAGE_URL", "FIELD_PRODUCT_NAME", "FIELD_STOCK_QUANTITY", "FIELD_UPDATED_AT", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}