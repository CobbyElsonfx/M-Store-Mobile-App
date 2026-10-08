package com.momokotle.agent.repository;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.models.StoreOrder;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JT\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0014"}, d2 = {"Lcom/momokotle/agent/repository/StoreRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/StoreRepository;", "orderRepository", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "(Lcom/momokotle/agent/domain/repository/OrderRepository;)V", "createAndSubmitOrder", "Lkotlin/Result;", "Lcom/momokotle/agent/models/StoreOrder;", "userId", "", "agentName", "itemName", "quantity", "", "unitPrice", "", "phoneNumber", "location", "createAndSubmitOrder-eH_QyT8", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IDLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class StoreRepositoryImpl implements com.momokotle.agent.domain.repository.StoreRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.OrderRepository orderRepository = null;
    
    @javax.inject.Inject()
    public StoreRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.OrderRepository orderRepository) {
        super();
    }
}