package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.engine.AgentCommissionEngine;
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
public final class CalculateVendorProfitUseCase_Factory implements Factory<CalculateVendorProfitUseCase> {
  private final Provider<AgentCommissionEngine> agentCommissionEngineProvider;

  private final Provider<ProfitRuleRepository> profitRuleRepositoryProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public CalculateVendorProfitUseCase_Factory(
      Provider<AgentCommissionEngine> agentCommissionEngineProvider,
      Provider<ProfitRuleRepository> profitRuleRepositoryProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.agentCommissionEngineProvider = agentCommissionEngineProvider;
    this.profitRuleRepositoryProvider = profitRuleRepositoryProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public CalculateVendorProfitUseCase get() {
    return newInstance(agentCommissionEngineProvider.get(), profitRuleRepositoryProvider.get(), settingsDataStoreProvider.get());
  }

  public static CalculateVendorProfitUseCase_Factory create(
      Provider<AgentCommissionEngine> agentCommissionEngineProvider,
      Provider<ProfitRuleRepository> profitRuleRepositoryProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new CalculateVendorProfitUseCase_Factory(agentCommissionEngineProvider, profitRuleRepositoryProvider, settingsDataStoreProvider);
  }

  public static CalculateVendorProfitUseCase newInstance(
      AgentCommissionEngine agentCommissionEngine, ProfitRuleRepository profitRuleRepository,
      SettingsDataStore settingsDataStore) {
    return new CalculateVendorProfitUseCase(agentCommissionEngine, profitRuleRepository, settingsDataStore);
  }
}
