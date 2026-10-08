package com.momokotle.agent.presentation.dashboard;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.repository.BusinessSettingsRepository;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.SystemUssdFlowRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.domain.usecase.CheckDuplicateTransactionUseCase;
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase;
import com.momokotle.agent.domain.usecase.ObserveCommissionSummaryUseCase;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.sms.CashOutSmsWatcher;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider;

  private final Provider<UssdAutomationEngine> automationEngineProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<AccessibilityRepository> accessibilityRepositoryProvider;

  private final Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider;

  private final Provider<CashOutSmsWatcher> cashOutSmsWatcherProvider;

  private final Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider;

  private final Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider;

  private final Provider<CustomFlowRepository> customFlowRepositoryProvider;

  private final Provider<SystemUssdFlowRepository> systemUssdFlowRepositoryProvider;

  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  private final Provider<SaveContactUseCase> saveContactUseCaseProvider;

  private final Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider;

  private final Provider<CheckDuplicateTransactionUseCase> checkDuplicateTransactionUseCaseProvider;

  public DashboardViewModel_Factory(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider,
      Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider,
      Provider<CashOutSmsWatcher> cashOutSmsWatcherProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider,
      Provider<CustomFlowRepository> customFlowRepositoryProvider,
      Provider<SystemUssdFlowRepository> systemUssdFlowRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider,
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider,
      Provider<CheckDuplicateTransactionUseCase> checkDuplicateTransactionUseCaseProvider) {
    this.contextProvider = contextProvider;
    this.startUssdFlowUseCaseProvider = startUssdFlowUseCaseProvider;
    this.automationEngineProvider = automationEngineProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.accessibilityRepositoryProvider = accessibilityRepositoryProvider;
    this.checkUssdAccessUseCaseProvider = checkUssdAccessUseCaseProvider;
    this.cashOutSmsWatcherProvider = cashOutSmsWatcherProvider;
    this.refreshUserProfileUseCaseProvider = refreshUserProfileUseCaseProvider;
    this.recordAttemptedTransactionUseCaseProvider = recordAttemptedTransactionUseCaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.observeCommissionSummaryUseCaseProvider = observeCommissionSummaryUseCaseProvider;
    this.customFlowRepositoryProvider = customFlowRepositoryProvider;
    this.systemUssdFlowRepositoryProvider = systemUssdFlowRepositoryProvider;
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
    this.saveContactUseCaseProvider = saveContactUseCaseProvider;
    this.businessSettingsRepositoryProvider = businessSettingsRepositoryProvider;
    this.checkDuplicateTransactionUseCaseProvider = checkDuplicateTransactionUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(contextProvider.get(), startUssdFlowUseCaseProvider.get(), automationEngineProvider.get(), settingsDataStoreProvider.get(), accessibilityRepositoryProvider.get(), checkUssdAccessUseCaseProvider.get(), cashOutSmsWatcherProvider.get(), refreshUserProfileUseCaseProvider.get(), recordAttemptedTransactionUseCaseProvider.get(), transactionRepositoryProvider.get(), observeCommissionSummaryUseCaseProvider.get(), customFlowRepositoryProvider.get(), systemUssdFlowRepositoryProvider.get(), savedContactRepositoryProvider.get(), saveContactUseCaseProvider.get(), businessSettingsRepositoryProvider.get(), checkDuplicateTransactionUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider,
      Provider<CheckUssdAccessUseCase> checkUssdAccessUseCaseProvider,
      Provider<CashOutSmsWatcher> cashOutSmsWatcherProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider,
      Provider<CustomFlowRepository> customFlowRepositoryProvider,
      Provider<SystemUssdFlowRepository> systemUssdFlowRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider,
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider,
      Provider<CheckDuplicateTransactionUseCase> checkDuplicateTransactionUseCaseProvider) {
    return new DashboardViewModel_Factory(contextProvider, startUssdFlowUseCaseProvider, automationEngineProvider, settingsDataStoreProvider, accessibilityRepositoryProvider, checkUssdAccessUseCaseProvider, cashOutSmsWatcherProvider, refreshUserProfileUseCaseProvider, recordAttemptedTransactionUseCaseProvider, transactionRepositoryProvider, observeCommissionSummaryUseCaseProvider, customFlowRepositoryProvider, systemUssdFlowRepositoryProvider, savedContactRepositoryProvider, saveContactUseCaseProvider, businessSettingsRepositoryProvider, checkDuplicateTransactionUseCaseProvider);
  }

  public static DashboardViewModel newInstance(Context context,
      StartUssdFlowUseCase startUssdFlowUseCase, UssdAutomationEngine automationEngine,
      SettingsDataStore settingsDataStore, AccessibilityRepository accessibilityRepository,
      CheckUssdAccessUseCase checkUssdAccessUseCase, CashOutSmsWatcher cashOutSmsWatcher,
      RefreshUserProfileUseCase refreshUserProfileUseCase,
      RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase,
      TransactionRepository transactionRepository,
      ObserveCommissionSummaryUseCase observeCommissionSummaryUseCase,
      CustomFlowRepository customFlowRepository, SystemUssdFlowRepository systemUssdFlowRepository,
      SavedContactRepository savedContactRepository, SaveContactUseCase saveContactUseCase,
      BusinessSettingsRepository businessSettingsRepository,
      CheckDuplicateTransactionUseCase checkDuplicateTransactionUseCase) {
    return new DashboardViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore, accessibilityRepository, checkUssdAccessUseCase, cashOutSmsWatcher, refreshUserProfileUseCase, recordAttemptedTransactionUseCase, transactionRepository, observeCommissionSummaryUseCase, customFlowRepository, systemUssdFlowRepository, savedContactRepository, saveContactUseCase, businessSettingsRepository, checkDuplicateTransactionUseCase);
  }
}
