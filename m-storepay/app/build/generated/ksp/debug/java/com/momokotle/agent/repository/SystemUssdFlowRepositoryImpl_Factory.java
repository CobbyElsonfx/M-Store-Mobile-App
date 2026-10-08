package com.momokotle.agent.repository;

import com.google.gson.Gson;
import com.momokotle.agent.data.datastore.UssdFlowCacheDataStore;
import com.momokotle.agent.data.remote.MStorepayApiService;
import com.momokotle.agent.database.dao.SystemUssdFlowDao;
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

  private final Provider<SystemUssdFlowDao> daoProvider;

  private final Provider<UssdFlowCacheDataStore> legacyCacheProvider;

  private final Provider<Gson> gsonProvider;

  public SystemUssdFlowRepositoryImpl_Factory(Provider<MStorepayApiService> apiProvider,
      Provider<SystemUssdFlowDao> daoProvider, Provider<UssdFlowCacheDataStore> legacyCacheProvider,
      Provider<Gson> gsonProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.legacyCacheProvider = legacyCacheProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public SystemUssdFlowRepositoryImpl get() {
    return newInstance(apiProvider.get(), daoProvider.get(), legacyCacheProvider.get(), gsonProvider.get());
  }

  public static SystemUssdFlowRepositoryImpl_Factory create(
      Provider<MStorepayApiService> apiProvider, Provider<SystemUssdFlowDao> daoProvider,
      Provider<UssdFlowCacheDataStore> legacyCacheProvider, Provider<Gson> gsonProvider) {
    return new SystemUssdFlowRepositoryImpl_Factory(apiProvider, daoProvider, legacyCacheProvider, gsonProvider);
  }

  public static SystemUssdFlowRepositoryImpl newInstance(MStorepayApiService api,
      SystemUssdFlowDao dao, UssdFlowCacheDataStore legacyCache, Gson gson) {
    return new SystemUssdFlowRepositoryImpl(api, dao, legacyCache, gson);
  }
}
