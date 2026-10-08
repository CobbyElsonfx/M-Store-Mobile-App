package com.momokotle.agent.data.firestore;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.momokotle.agent.models.OrderStatus;
import com.momokotle.agent.models.StoreOrder;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreOrderDataSource;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "createOrder", "", "order", "Lcom/momokotle/agent/models/StoreOrder;", "(Lcom/momokotle/agent/models/StoreOrder;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeOrdersForUser", "Lkotlinx/coroutines/flow/Flow;", "", "userId", "", "Companion", "app_debug"})
public final class FirestoreOrderDataSource {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COLLECTION_STORE_ORDERS = "storeOrders";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_ORDER_ID = "orderId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_USER_ID = "userId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_AGENT_NAME = "agentName";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_PHONE_NUMBER = "phoneNumber";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_LOCATION = "location";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_ITEM_NAME = "itemName";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_QUANTITY = "quantity";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_TOTAL_AMOUNT = "totalAmount";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_STATUS = "status";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_CREATED_AT = "createdAt";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FIELD_UPDATED_AT = "updatedAt";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.data.firestore.FirestoreOrderDataSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public FirestoreOrderDataSource(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createOrder(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.StoreOrder order, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreOrder>> observeOrdersForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/momokotle/agent/data/firestore/FirestoreOrderDataSource$Companion;", "", "()V", "COLLECTION_STORE_ORDERS", "", "FIELD_AGENT_NAME", "FIELD_CREATED_AT", "FIELD_ITEM_NAME", "FIELD_LOCATION", "FIELD_ORDER_ID", "FIELD_PHONE_NUMBER", "FIELD_QUANTITY", "FIELD_STATUS", "FIELD_TOTAL_AMOUNT", "FIELD_UPDATED_AT", "FIELD_USER_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}