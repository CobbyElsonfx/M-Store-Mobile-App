package com.momokotle.agent.presentation.settings;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
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
public final class AppSettingsViewModel_Factory implements Factory<AppSettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public AppSettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.contextProvider = contextProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public AppSettingsViewModel get() {
    return newInstance(contextProvider.get(), settingsDataStoreProvider.get());
  }

  public static AppSettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new AppSettingsViewModel_Factory(contextProvider, settingsDataStoreProvider);
  }

  public static AppSettingsViewModel newInstance(Context context,
      SettingsDataStore settingsDataStore) {
    return new AppSettingsViewModel(context, settingsDataStore);
  }
}
