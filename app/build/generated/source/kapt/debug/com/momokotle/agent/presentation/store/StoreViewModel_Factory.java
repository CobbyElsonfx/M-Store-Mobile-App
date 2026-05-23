package com.momokotle.agent.presentation.store;

import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.utils.NetworkMonitor;
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
public final class StoreViewModel_Factory implements Factory<StoreViewModel> {
  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<StoreRepository> storeRepositoryProvider;

  private final Provider<UserIdProvider> userIdProvider;

  public StoreViewModel_Factory(Provider<NetworkMonitor> networkMonitorProvider,
      Provider<StoreRepository> storeRepositoryProvider, Provider<UserIdProvider> userIdProvider) {
    this.networkMonitorProvider = networkMonitorProvider;
    this.storeRepositoryProvider = storeRepositoryProvider;
    this.userIdProvider = userIdProvider;
  }

  @Override
  public StoreViewModel get() {
    return newInstance(networkMonitorProvider.get(), storeRepositoryProvider.get(), userIdProvider.get());
  }

  public static StoreViewModel_Factory create(Provider<NetworkMonitor> networkMonitorProvider,
      Provider<StoreRepository> storeRepositoryProvider, Provider<UserIdProvider> userIdProvider) {
    return new StoreViewModel_Factory(networkMonitorProvider, storeRepositoryProvider, userIdProvider);
  }

  public static StoreViewModel newInstance(NetworkMonitor networkMonitor,
      StoreRepository storeRepository, UserIdProvider userIdProvider) {
    return new StoreViewModel(networkMonitor, storeRepository, userIdProvider);
  }
}
