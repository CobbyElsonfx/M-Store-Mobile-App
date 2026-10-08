package com.momokotle.agent.presentation.settings;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
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
public final class AppSettingsViewModel_Factory implements Factory<AppSettingsViewModel> {
  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<AccessibilityRepository> accessibilityRepositoryProvider;

  public AppSettingsViewModel_Factory(Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider) {
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.accessibilityRepositoryProvider = accessibilityRepositoryProvider;
  }

  @Override
  public AppSettingsViewModel get() {
    return newInstance(settingsDataStoreProvider.get(), accessibilityRepositoryProvider.get());
  }

  public static AppSettingsViewModel_Factory create(
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider) {
    return new AppSettingsViewModel_Factory(settingsDataStoreProvider, accessibilityRepositoryProvider);
  }

  public static AppSettingsViewModel newInstance(SettingsDataStore settingsDataStore,
      AccessibilityRepository accessibilityRepository) {
    return new AppSettingsViewModel(settingsDataStore, accessibilityRepository);
  }
}
