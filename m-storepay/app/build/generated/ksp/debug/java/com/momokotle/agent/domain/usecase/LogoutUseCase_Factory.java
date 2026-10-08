package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.ProductRepository;
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
public final class LogoutUseCase_Factory implements Factory<LogoutUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  private final Provider<FormSubmissionRepository> formSubmissionRepositoryProvider;

  public LogoutUseCase_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
    this.formSubmissionRepositoryProvider = formSubmissionRepositoryProvider;
  }

  @Override
  public LogoutUseCase get() {
    return newInstance(authRepositoryProvider.get(), productRepositoryProvider.get(), orderRepositoryProvider.get(), formSubmissionRepositoryProvider.get());
  }

  public static LogoutUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    return new LogoutUseCase_Factory(authRepositoryProvider, productRepositoryProvider, orderRepositoryProvider, formSubmissionRepositoryProvider);
  }

  public static LogoutUseCase newInstance(AuthRepository authRepository,
      ProductRepository productRepository, OrderRepository orderRepository,
      FormSubmissionRepository formSubmissionRepository) {
    return new LogoutUseCase(authRepository, productRepository, orderRepository, formSubmissionRepository);
  }
}
