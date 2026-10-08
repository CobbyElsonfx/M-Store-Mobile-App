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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/momokotle/agent/presentation/orders/OrderDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "orderRepository", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "(Lcom/momokotle/agent/domain/repository/OrderRepository;)V", "_order", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/models/StoreOrder;", "order", "Lkotlinx/coroutines/flow/StateFlow;", "getOrder", "()Lkotlinx/coroutines/flow/StateFlow;", "loadOrder", "", "orderId", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class OrderDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.OrderRepository orderRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.models.StoreOrder> _order = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.StoreOrder> order = null;
    
    @javax.inject.Inject()
    public OrderDetailViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.OrderRepository orderRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.StoreOrder> getOrder() {
        return null;
    }
    
    public final void loadOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String orderId) {
    }
}