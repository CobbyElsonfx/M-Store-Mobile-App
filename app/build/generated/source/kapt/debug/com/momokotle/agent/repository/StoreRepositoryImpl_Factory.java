package com.momokotle.agent.repository;

import com.momokotle.agent.domain.repository.OrderRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class StoreRepositoryImpl_Factory implements Factory<StoreRepositoryImpl> {
  private final Provider<OrderRepository> orderRepositoryProvider;

  public StoreRepositoryImpl_Factory(Provider<OrderRepository> orderRepositoryProvider) {
    this.orderRepositoryProvider = orderRepositoryProvider;
  }

  @Override
  public StoreRepositoryImpl get() {
    return newInstance(orderRepositoryProvider.get());
  }

  public static StoreRepositoryImpl_Factory create(
      Provider<OrderRepository> orderRepositoryProvider) {
    return new StoreRepositoryImpl_Factory(orderRepositoryProvider);
  }

  public static StoreRepositoryImpl newInstance(OrderRepository orderRepository) {
    return new StoreRepositoryImpl(orderRepository);
  }
}
