package com.momokotle.agent.repository;

import com.momokotle.agent.data.firestore.FirestoreOrderDataSource;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.di.ApplicationScope;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.models.OrderStatus;
import com.momokotle.agent.models.StoreOrder;
import com.momokotle.agent.utils.NetworkMonitor;
import kotlinx.coroutines.flow.Flow;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ$\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0082@\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0017\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00130\u001a2\u0006\u0010\u0011\u001a\u00020\u000eH\u0016JT\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00140\u001c2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u000e2\u0006\u0010$\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b%\u0010&J\u0010\u0010\'\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000eH\u0016J\b\u0010(\u001a\u00020\u0010H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006)"}, d2 = {"Lcom/momokotle/agent/repository/OrderRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "storeOrderDao", "Lcom/momokotle/agent/database/dao/StoreOrderDao;", "firestoreOrderDataSource", "Lcom/momokotle/agent/data/firestore/FirestoreOrderDataSource;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "applicationScope", "Lkotlinx/coroutines/CoroutineScope;", "(Lcom/momokotle/agent/database/dao/StoreOrderDao;Lcom/momokotle/agent/data/firestore/FirestoreOrderDataSource;Lcom/momokotle/agent/utils/NetworkMonitor;Lkotlinx/coroutines/CoroutineScope;)V", "syncJob", "Lkotlinx/coroutines/Job;", "syncedUserId", "", "cacheOrdersForUser", "", "userId", "orders", "", "Lcom/momokotle/agent/models/StoreOrder;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOrder", "orderId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeMyOrders", "Lkotlinx/coroutines/flow/Flow;", "placeOrder", "Lkotlin/Result;", "agentName", "itemName", "quantity", "", "unitPrice", "", "phoneNumber", "location", "placeOrder-eH_QyT8", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IDLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startSync", "stopSync", "app_debug"})
public final class OrderRepositoryImpl implements com.momokotle.agent.domain.repository.OrderRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.StoreOrderDao storeOrderDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.firestore.FirestoreOrderDataSource firestoreOrderDataSource = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope applicationScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job syncJob;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String syncedUserId;
    
    @javax.inject.Inject()
    public OrderRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.StoreOrderDao storeOrderDao, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.firestore.FirestoreOrderDataSource firestoreOrderDataSource, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor, @com.momokotle.agent.di.ApplicationScope()
    @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope applicationScope) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.momokotle.agent.models.StoreOrder>> observeMyOrders(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String orderId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.StoreOrder> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public void startSync(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    @java.lang.Override()
    public void stopSync() {
    }
    
    private final java.lang.Object cacheOrdersForUser(java.lang.String userId, java.util.List<com.momokotle.agent.models.StoreOrder> orders, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}