package com.momokotle.agent.presentation.auth;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.AuthResult;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase;
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase;
import com.momokotle.agent.repository.AuthRepositoryImpl;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010\u0016\u001a\u00020\u0017J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u0017H\u0002R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u001c"}, d2 = {"Lcom/momokotle/agent/presentation/auth/SessionViewModel;", "Landroidx/lifecycle/ViewModel;", "observeAuthStateUseCase", "Lcom/momokotle/agent/domain/usecase/ObserveAuthStateUseCase;", "refreshUserProfileUseCase", "Lcom/momokotle/agent/domain/usecase/RefreshUserProfileUseCase;", "authRepository", "Lcom/momokotle/agent/domain/repository/AuthRepository;", "productRepository", "Lcom/momokotle/agent/domain/repository/ProductRepository;", "orderRepository", "Lcom/momokotle/agent/domain/repository/OrderRepository;", "formSubmissionRepository", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "(Lcom/momokotle/agent/domain/usecase/ObserveAuthStateUseCase;Lcom/momokotle/agent/domain/usecase/RefreshUserProfileUseCase;Lcom/momokotle/agent/domain/repository/AuthRepository;Lcom/momokotle/agent/domain/repository/ProductRepository;Lcom/momokotle/agent/domain/repository/OrderRepository;Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/auth/SessionUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearAuthError", "", "startDataSync", "userId", "", "stopDataSync", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SessionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase refreshUserProfileUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.OrderRepository orderRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.auth.SessionUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.auth.SessionUiState> uiState = null;
    
    @javax.inject.Inject()
    public SessionViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase observeAuthStateUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase refreshUserProfileUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.ProductRepository productRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.OrderRepository orderRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.FormSubmissionRepository formSubmissionRepository) {
        super();
    }
    
    private final void startDataSync(java.lang.String userId) {
    }
    
    private final void stopDataSync() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.auth.SessionUiState> getUiState() {
        return null;
    }
    
    public final void clearAuthError() {
    }
}