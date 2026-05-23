package com.momokotle.agent.presentation.settings;

import android.content.Context;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsHomeViewModel_Factory implements Factory<SettingsHomeViewModel> {
  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  private final Provider<Context> contextProvider;

  public SettingsHomeViewModel_Factory(Provider<UserAccountDataStore> userAccountDataStoreProvider,
      Provider<Context> contextProvider) {
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsHomeViewModel get() {
    return newInstance(userAccountDataStoreProvider.get(), contextProvider.get());
  }

  public static SettingsHomeViewModel_Factory create(
      Provider<UserAccountDataStore> userAccountDataStoreProvider,
      Provider<Context> contextProvider) {
    return new SettingsHomeViewModel_Factory(userAccountDataStoreProvider, contextProvider);
  }

  public static SettingsHomeViewModel newInstance(UserAccountDataStore userAccountDataStore,
      Context context) {
    return new SettingsHomeViewModel(userAccountDataStore, context);
  }
}
