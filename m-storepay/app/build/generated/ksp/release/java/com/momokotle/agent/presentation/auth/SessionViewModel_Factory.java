package com.momokotle.agent.presentation.auth;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase;
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SessionViewModel_Factory implements Factory<SessionViewModel> {
  private final Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider;

  private final Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<FormSubmissionRepository> formSubmissionRepositoryProvider;

  public SessionViewModel_Factory(Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    this.observeAuthStateUseCaseProvider = observeAuthStateUseCaseProvider;
    this.refreshUserProfileUseCaseProvider = refreshUserProfileUseCaseProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.formSubmissionRepositoryProvider = formSubmissionRepositoryProvider;
  }

  @Override
  public SessionViewModel get() {
    return newInstance(observeAuthStateUseCaseProvider.get(), refreshUserProfileUseCaseProvider.get(), authRepositoryProvider.get(), productRepositoryProvider.get(), orderRepositoryProvider.get(), formSubmissionRepositoryProvider.get());
  }

  public static SessionViewModel_Factory create(
      Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    return new SessionViewModel_Factory(observeAuthStateUseCaseProvider, refreshUserProfileUseCaseProvider, authRepositoryProvider, productRepositoryProvider, orderRepositoryProvider, formSubmissionRepositoryProvider);
  }

  public static SessionViewModel newInstance(ObserveAuthStateUseCase observeAuthStateUseCase,
      RefreshUserProfileUseCase refreshUserProfileUseCase, AuthRepository authRepository,
      ProductRepository productRepository, OrderRepository orderRepository,
      FormSubmissionRepository formSubmissionRepository) {
    return new SessionViewModel(observeAuthStateUseCase, refreshUserProfileUseCase, authRepository, productRepository, orderRepository, formSubmissionRepository);
  }
}
