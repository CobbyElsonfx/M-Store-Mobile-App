package com.momokotle.agent.data.remote;

import com.momokotle.agent.data.datastore.AuthTokenDataStore;
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
public final class AuthInterceptor_Factory implements Factory<AuthInterceptor> {
  private final Provider<AuthTokenDataStore> authTokenDataStoreProvider;

  public AuthInterceptor_Factory(Provider<AuthTokenDataStore> authTokenDataStoreProvider) {
    this.authTokenDataStoreProvider = authTokenDataStoreProvider;
  }

  @Override
  public AuthInterceptor get() {
    return newInstance(authTokenDataStoreProvider.get());
  }

  public static AuthInterceptor_Factory create(
      Provider<AuthTokenDataStore> authTokenDataStoreProvider) {
    return new AuthInterceptor_Factory(authTokenDataStoreProvider);
  }

  public static AuthInterceptor newInstance(AuthTokenDataStore authTokenDataStore) {
    return new AuthInterceptor(authTokenDataStore);
  }
}
