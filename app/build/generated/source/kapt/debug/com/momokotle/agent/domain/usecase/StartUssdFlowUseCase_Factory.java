package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdDialHelper;
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
public final class StartUssdFlowUseCase_Factory implements Factory<StartUssdFlowUseCase> {
  private final Provider<UssdDialHelper> dialHelperProvider;

  private final Provider<UssdAutomationEngine> engineProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public StartUssdFlowUseCase_Factory(Provider<UssdDialHelper> dialHelperProvider,
      Provider<UssdAutomationEngine> engineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.dialHelperProvider = dialHelperProvider;
    this.engineProvider = engineProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public StartUssdFlowUseCase get() {
    return newInstance(dialHelperProvider.get(), engineProvider.get(), settingsDataStoreProvider.get());
  }

  public static StartUssdFlowUseCase_Factory create(Provider<UssdDialHelper> dialHelperProvider,
      Provider<UssdAutomationEngine> engineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new StartUssdFlowUseCase_Factory(dialHelperProvider, engineProvider, settingsDataStoreProvider);
  }

  public static StartUssdFlowUseCase newInstance(UssdDialHelper dialHelper,
      UssdAutomationEngine engine, SettingsDataStore settingsDataStore) {
    return new StartUssdFlowUseCase(dialHelper, engine, settingsDataStore);
  }
}
