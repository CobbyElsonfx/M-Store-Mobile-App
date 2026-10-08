package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.AuthResult;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.models.UserProfile;
import com.momokotle.agent.utils.PhoneNumberFormatter;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u000b\u001a\u00020\fH\u0086B\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/momokotle/agent/domain/usecase/LogoutUseCase;", "", "authRepository", "Lcom/momokotle/agent/domain/repository/AuthRepository;", "productRepository", "Lcom/momokotle/agent/domain/repository/ProductRepository;", "orderRepository", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "formSubmissionRepository", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "(Lcom/momokotle/agent/domain/repository/AuthRepository;Lcom/momokotle/agent/domain/repository/ProductRepository;Lcom/momokotle/agent/domain/repository/OrderRepository;Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;)V", "invoke", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class LogoutUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.OrderRepository orderRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository = null;
    
    @javax.inject.Inject()
    public LogoutUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.ProductRepository productRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.OrderRepository orderRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}