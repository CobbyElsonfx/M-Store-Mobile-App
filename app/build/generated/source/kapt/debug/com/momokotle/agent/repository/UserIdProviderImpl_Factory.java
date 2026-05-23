package com.momokotle.agent.repository;

import com.momokotle.agent.data.datastore.UserAccountDataStore;
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
public final class UserIdProviderImpl_Factory implements Factory<UserIdProviderImpl> {
  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  public UserIdProviderImpl_Factory(Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
  }

  @Override
  public UserIdProviderImpl get() {
    return newInstance(userAccountDataStoreProvider.get());
  }

  public static UserIdProviderImpl_Factory create(
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    return new UserIdProviderImpl_Factory(userAccountDataStoreProvider);
  }

  public static UserIdProviderImpl newInstance(UserAccountDataStore userAccountDataStore) {
    return new UserIdProviderImpl(userAccountDataStore);
  }
}
