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
public final class UserAccountDataStore_Factory implements Factory<UserAccountDataStore> {
  private final Provider<Context> contextProvider;

  public UserAccountDataStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public UserAccountDataStore get() {
    return newInstance(contextProvider.get());
  }

  public static UserAccountDataStore_Factory create(Provider<Context> contextProvider) {
    return new UserAccountDataStore_Factory(contextProvider);
  }

  public static UserAccountDataStore newInstance(Context context) {
    return new UserAccountDataStore(context);
  }
}
