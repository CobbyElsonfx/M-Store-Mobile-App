package com.momokotle.agent.repository;

import com.momokotle.agent.data.datastore.UssdFlowCacheDataStore;
import com.momokotle.agent.data.remote.MStorepayApiService;
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
public final class SystemUssdFlowRepositoryImpl_Factory implements Factory<SystemUssdFlowRepositoryImpl> {
  private final Provider<MStorepayApiService> apiProvider;

  private final Provider<UssdFlowCacheDataStore> cacheProvider;

  public SystemUssdFlowRepositoryImpl_Factory(Provider<MStorepayApiService> apiProvider,
      Provider<UssdFlowCacheDataStore> cacheProvider) {
    this.apiProvider = apiProvider;
    this.cacheProvider = cacheProvider;
  }

  @Override
  public SystemUssdFlowRepositoryImpl get() {
    return newInstance(apiProvider.get(), cacheProvider.get());
  }

  public static SystemUssdFlowRepositoryImpl_Factory create(
      Provider<MStorepayApiService> apiProvider, Provider<UssdFlowCacheDataStore> cacheProvider) {
    return new SystemUssdFlowRepositoryImpl_Factory(apiProvider, cacheProvider);
  }

  public static SystemUssdFlowRepositoryImpl newInstance(MStorepayApiService api,
      UssdFlowCacheDataStore cache) {
    return new SystemUssdFlowRepositoryImpl(api, cache);
  }
}
