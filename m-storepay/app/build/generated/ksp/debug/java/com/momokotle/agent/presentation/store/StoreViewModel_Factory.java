package com.momokotle.agent.presentation.store;

import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase;
import com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase;
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

  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<StoreRepository> storeRepositoryProvider;

  private final Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider;

  private final Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider;

  public StoreViewModel_Factory(Provider<NetworkMonitor> networkMonitorProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<StoreRepository> storeRepositoryProvider,
      Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider,
      Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider) {
    this.networkMonitorProvider = networkMonitorProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.storeRepositoryProvider = storeRepositoryProvider;
    this.getAuthenticatedUserUseCaseProvider = getAuthenticatedUserUseCaseProvider;
    this.observeAuthStateUseCaseProvider = observeAuthStateUseCaseProvider;
  }

  @Override
  public StoreViewModel get() {
    return newInstance(networkMonitorProvider.get(), productRepositoryProvider.get(), storeRepositoryProvider.get(), getAuthenticatedUserUseCaseProvider.get(), observeAuthStateUseCaseProvider.get());
  }

  public static StoreViewModel_Factory create(Provider<NetworkMonitor> networkMonitorProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<StoreRepository> storeRepositoryProvider,
      Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider,
      Provider<ObserveAuthStateUseCase> observeAuthStateUseCaseProvider) {
    return new StoreViewModel_Factory(networkMonitorProvider, productRepositoryProvider, storeRepositoryProvider, getAuthenticatedUserUseCaseProvider, observeAuthStateUseCaseProvider);
  }

  public static StoreViewModel newInstance(NetworkMonitor networkMonitor,
      ProductRepository productRepository, StoreRepository storeRepository,
      GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
      ObserveAuthStateUseCase observeAuthStateUseCase) {
    return new StoreViewModel(networkMonitor, productRepository, storeRepository, getAuthenticatedUserUseCase, observeAuthStateUseCase);
  }
}
