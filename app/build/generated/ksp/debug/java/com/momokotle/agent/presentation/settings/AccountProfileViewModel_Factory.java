package com.momokotle.agent.presentation.settings;

import com.momokotle.agent.data.datastore.UserAccountDataStore;
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
public final class AccountProfileViewModel_Factory implements Factory<AccountProfileViewModel> {
  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  public AccountProfileViewModel_Factory(
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
  }

  @Override
  public AccountProfileViewModel get() {
    return newInstance(userAccountDataStoreProvider.get());
  }

  public static AccountProfileViewModel_Factory create(
      Provider<UserAccountDataStore> userAccountDataStoreProvider) {
    return new AccountProfileViewModel_Factory(userAccountDataStoreProvider);
  }

  public static AccountProfileViewModel newInstance(UserAccountDataStore userAccountDataStore) {
    return new AccountProfileViewModel(userAccountDataStore);
  }
}
