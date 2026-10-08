package com.momokotle.agent.repository;

import com.momokotle.agent.data.datastore.AuthTokenDataStore;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<MStorepayApiService> apiProvider;

  private final Provider<AuthTokenDataStore> authTokenDataStoreProvider;

  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  public AuthRepositoryImpl_Factory(Provider<MStorepayApiService> apiProvider,
      Provider<AuthTokenDataStore> authTokenDataStoreProvider,
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    this.apiProvider = apiProvider;
    this.authTokenDataStoreProvider = authTokenDataStoreProvider;
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(apiProvider.get(), authTokenDataStoreProvider.get(), userAccountDataStoreProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<MStorepayApiService> apiProvider,
      Provider<AuthTokenDataStore> authTokenDataStoreProvider,
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    return new AuthRepositoryImpl_Factory(apiProvider, authTokenDataStoreProvider, userAccountDataStoreProvider);
  }

  public static AuthRepositoryImpl newInstance(MStorepayApiService api,
      AuthTokenDataStore authTokenDataStore, UserAccountDataStore userAccountDataStore) {
    return new AuthRepositoryImpl(api, authTokenDataStore, userAccountDataStore);
  }
}
