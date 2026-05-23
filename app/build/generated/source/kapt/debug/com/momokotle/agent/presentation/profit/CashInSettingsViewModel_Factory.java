package com.momokotle.agent.presentation.profit;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
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
public final class CashInSettingsViewModel_Factory implements Factory<CashInSettingsViewModel> {
  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<ProfitRuleRepository> profitRuleRepositoryProvider;

  public CashInSettingsViewModel_Factory(Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<ProfitRuleRepository> profitRuleRepositoryProvider) {
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.profitRuleRepositoryProvider = profitRuleRepositoryProvider;
  }

  @Override
  public CashInSettingsViewModel get() {
    return newInstance(settingsDataStoreProvider.get(), profitRuleRepositoryProvider.get());
  }

  public static CashInSettingsViewModel_Factory create(
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<ProfitRuleRepository> profitRuleRepositoryProvider) {
    return new CashInSettingsViewModel_Factory(settingsDataStoreProvider, profitRuleRepositoryProvider);
  }

  public static CashInSettingsViewModel newInstance(SettingsDataStore settingsDataStore,
      ProfitRuleRepository profitRuleRepository) {
    return new CashInSettingsViewModel(settingsDataStore, profitRuleRepository);
  }
}
