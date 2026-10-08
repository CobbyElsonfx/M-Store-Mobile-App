package com.momokotle.agent.presentation.custommenu;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
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
public final class CustomFlowViewModel_Factory implements Factory<CustomFlowViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider;

  private final Provider<UssdAutomationEngine> automationEngineProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<AccessibilityRepository> accessibilityRepositoryProvider;

  private final Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider;

  private final Provider<CustomFlowRepository> customFlowRepositoryProvider;

  private final Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider;

  public CustomFlowViewModel_Factory(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider,
      Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider,
      Provider<CustomFlowRepository> customFlowRepositoryProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider) {
    this.contextProvider = contextProvider;
    this.startUssdFlowUseCaseProvider = startUssdFlowUseCaseProvider;
    this.automationEngineProvider = automationEngineProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.accessibilityRepositoryProvider = accessibilityRepositoryProvider;
    this.checkUssdAccessUseCaseProvider = checkUssdAccessUseCaseProvider;
    this.customFlowRepositoryProvider = customFlowRepositoryProvider;
    this.recordAttemptedTransactionUseCaseProvider = recordAttemptedTransactionUseCaseProvider;
  }

  @Override
  public CustomFlowViewModel get() {
    return newInstance(contextProvider.get(), startUssdFlowUseCaseProvider.get(), automationEngineProvider.get(), settingsDataStoreProvider.get(), accessibilityRepositoryProvider.get(), checkUssdAccessUseCaseProvider.get(), customFlowRepositoryProvider.get(), recordAttemptedTransactionUseCaseProvider.get());
  }

  public static CustomFlowViewModel_Factory create(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider,
      Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider,
      Provider<CustomFlowRepository> customFlowRepositoryProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider) {
    return new CustomFlowViewModel_Factory(contextProvider, startUssdFlowUseCaseProvider, automationEngineProvider, settingsDataStoreProvider, accessibilityRepositoryProvider, checkUssdAccessUseCaseProvider, customFlowRepositoryProvider, recordAttemptedTransactionUseCaseProvider);
  }

  public static CustomFlowViewModel newInstance(Context context,
      StartUssdFlowUseCase startUssdFlowUseCase, UssdAutomationEngine automationEngine,
      SettingsDataStore settingsDataStore, AccessibilityRepository accessibilityRepository,
      CheckUssdAccessUseCase checkUssdAccessUseCase, CustomFlowRepository customFlowRepository,
      RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase) {
    return new CustomFlowViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore, accessibilityRepository, checkUssdAccessUseCase, customFlowRepository, recordAttemptedTransactionUseCase);
  }
}
