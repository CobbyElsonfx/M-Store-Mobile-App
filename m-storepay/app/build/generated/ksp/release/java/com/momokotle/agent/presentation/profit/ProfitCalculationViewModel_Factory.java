package com.momokotle.agent.presentation.profit;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase;
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
public final class ProfitCalculationViewModel_Factory implements Factory<ProfitCalculationViewModel> {
  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider;

  public ProfitCalculationViewModel_Factory(Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider) {
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.calculateVendorProfitUseCaseProvider = calculateVendorProfitUseCaseProvider;
  }

  @Override
  public ProfitCalculationViewModel get() {
    return newInstance(settingsDataStoreProvider.get(), calculateVendorProfitUseCaseProvider.get());
  }

  public static ProfitCalculationViewModel_Factory create(
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider) {
    return new ProfitCalculationViewModel_Factory(settingsDataStoreProvider, calculateVendorProfitUseCaseProvider);
  }

  public static ProfitCalculationViewModel newInstance(SettingsDataStore settingsDataStore,
      CalculateVendorProfitUseCase calculateVendorProfitUseCase) {
    return new ProfitCalculationViewModel(settingsDataStore, calculateVendorProfitUseCase);
  }
}
