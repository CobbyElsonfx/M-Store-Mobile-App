package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.MStorepayApiService;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.utils.NetworkMonitor;
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
  private final Provider<StoreOrderDao> storeOrderDaoProvider;

  private final Provider<MStorepayApiService> apiProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public StoreRepositoryImpl_Factory(Provider<StoreOrderDao> storeOrderDaoProvider,
      Provider<MStorepayApiService> apiProvider, Provider<NetworkMonitor> networkMonitorProvider) {
    this.storeOrderDaoProvider = storeOrderDaoProvider;
    this.apiProvider = apiProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public StoreRepositoryImpl get() {
    return newInstance(storeOrderDaoProvider.get(), apiProvider.get(), networkMonitorProvider.get());
  }

  public static StoreRepositoryImpl_Factory create(Provider<StoreOrderDao> storeOrderDaoProvider,
      Provider<MStorepayApiService> apiProvider, Provider<NetworkMonitor> networkMonitorProvider) {
    return new StoreRepositoryImpl_Factory(storeOrderDaoProvider, apiProvider, networkMonitorProvider);
  }

  public static StoreRepositoryImpl newInstance(StoreOrderDao storeOrderDao,
      MStorepayApiService api, NetworkMonitor networkMonitor) {
    return new StoreRepositoryImpl(storeOrderDao, api, networkMonitor);
  }
}
