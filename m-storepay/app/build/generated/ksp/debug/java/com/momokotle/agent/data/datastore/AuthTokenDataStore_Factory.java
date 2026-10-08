package com.momokotle.agent.data.datastore;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AuthTokenDataStore_Factory implements Factory<AuthTokenDataStore> {
  private final Provider<Context> contextProvider;

  public AuthTokenDataStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AuthTokenDataStore get() {
    return newInstance(contextProvider.get());
  }

  public static AuthTokenDataStore_Factory create(Provider<Context> contextProvider) {
    return new AuthTokenDataStore_Factory(contextProvider);
  }

  public static AuthTokenDataStore newInstance(Context context) {
    return new AuthTokenDataStore(context);
  }
}
