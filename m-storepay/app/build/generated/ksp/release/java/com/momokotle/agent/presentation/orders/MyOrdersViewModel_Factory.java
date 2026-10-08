package com.momokotle.agent.presentation.orders;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
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
public final class MyOrdersViewModel_Factory implements Factory<MyOrdersViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<OrderRepository> orderRepositoryProvider;

  public MyOrdersViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public MyOrdersViewModel get() {
    return newInstance(authRepositoryProvider.get(), orderRepositoryProvider.get());
  }

  public static MyOrdersViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<OrderRepository> orderRepositoryProvider) {
    return new MyOrdersViewModel_Factory(authRepositoryProvider, orderRepositoryProvider);
  }

  public static MyOrdersViewModel newInstance(AuthRepository authRepository,
      OrderRepository orderRepository) {
    return new MyOrdersViewModel(authRepository, orderRepository);
  }
}
