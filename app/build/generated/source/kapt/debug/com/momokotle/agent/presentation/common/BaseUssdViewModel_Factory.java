package com.momokotle.agent.presentation.common;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.ussd.UssdAutomationEngine;
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
public final class BaseUssdViewModel_Factory implements Factory<BaseUssdViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider;

  private final Provider<UssdAutomationEngine> automationEngineProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public BaseUssdViewModel_Factory(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.contextProvider = contextProvider;
    this.startUssdFlowUseCaseProvider = startUssdFlowUseCaseProvider;
    this.automationEngineProvider = automationEngineProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public BaseUssdViewModel get() {
    return newInstance(contextProvider.get(), startUssdFlowUseCaseProvider.get(), automationEngineProvider.get(), settingsDataStoreProvider.get());
  }

  public static BaseUssdViewModel_Factory create(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new BaseUssdViewModel_Factory(contextProvider, startUssdFlowUseCaseProvider, automationEngineProvider, settingsDataStoreProvider);
  }

  public static BaseUssdViewModel newInstance(Context context,
      StartUssdFlowUseCase startUssdFlowUseCase, UssdAutomationEngine automationEngine,
      SettingsDataStore settingsDataStore) {
    return new BaseUssdViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore);
  }
}
