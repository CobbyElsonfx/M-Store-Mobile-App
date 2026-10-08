package com.momokotle.agent.presentation.orders;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.models.StoreOrder;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/momokotle/agent/presentation/orders/MyOrdersViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/momokotle/agent/domain/repository/AuthRepository;", "orderRepository", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "(Lcom/momokotle/agent/domain/repository/AuthRepository;Lcom/momokotle/agent/domain/repository/OrderRepository;)V", "orders", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/momokotle/agent/models/StoreOrder;", "getOrders", "()Lkotlinx/coroutines/flow/StateFlow;", "app_debug"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MyOrdersViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.StoreOrder>> orders = null;
    
    @javax.inject.Inject()
    public MyOrdersViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.OrderRepository orderRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.StoreOrder>> getOrders() {
        return null;
    }
}