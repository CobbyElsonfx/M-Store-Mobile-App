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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0086B\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/momokotle/agent/domain/usecase/GetAuthenticatedUserUseCase;", "", "authRepository", "Lcom/momokotle/agent/domain/repository/AuthRepository;", "(Lcom/momokotle/agent/domain/repository/AuthRepository;)V", "invoke", "Lcom/momokotle/agent/models/UserProfile;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class GetAuthenticatedUserUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.AuthRepository authRepository = null;
    
    @javax.inject.Inject()
    public GetAuthenticatedUserUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.models.UserProfile> $completion) {
        return null;
    }
}