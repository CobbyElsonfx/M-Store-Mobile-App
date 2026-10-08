package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelOrderRemoteDataSource;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.momokotle.agent.di.ApplicationScope")
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
public final class OrderRepositoryImpl_Factory implements Factory<OrderRepositoryImpl> {
  private final Provider<StoreOrderDao> storeOrderDaoProvider;

  private final Provider<LaravelOrderRemoteDataSource> laravelOrderRemoteDataSourceProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<CoroutineScope> applicationScopeProvider;

  public OrderRepositoryImpl_Factory(Provider<StoreOrderDao> storeOrderDaoProvider,
      Provider<LaravelOrderRemoteDataSource> laravelOrderRemoteDataSourceProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    this.storeOrderDaoProvider = storeOrderDaoProvider;
    this.laravelOrderRemoteDataSourceProvider = laravelOrderRemoteDataSourceProvider;
    this.networkMonitorProvider = networkMonitorProvider;
    this.applicationScopeProvider = applicationScopeProvider;
  }

  @Override
  public OrderRepositoryImpl get() {
    return newInstance(storeOrderDaoProvider.get(), laravelOrderRemoteDataSourceProvider.get(), networkMonitorProvider.get(), applicationScopeProvider.get());
  }

  public static OrderRepositoryImpl_Factory create(Provider<StoreOrderDao> storeOrderDaoProvider,
      Provider<LaravelOrderRemoteDataSource> laravelOrderRemoteDataSourceProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    return new OrderRepositoryImpl_Factory(storeOrderDaoProvider, laravelOrderRemoteDataSourceProvider, networkMonitorProvider, applicationScopeProvider);
  }

  public static OrderRepositoryImpl newInstance(StoreOrderDao storeOrderDao,
      LaravelOrderRemoteDataSource laravelOrderRemoteDataSource, NetworkMonitor networkMonitor,
      CoroutineScope applicationScope) {
    return new OrderRepositoryImpl(storeOrderDao, laravelOrderRemoteDataSource, networkMonitor, applicationScope);
  }
}
