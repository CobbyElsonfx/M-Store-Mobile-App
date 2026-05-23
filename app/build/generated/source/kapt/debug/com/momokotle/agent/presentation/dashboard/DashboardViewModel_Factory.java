package com.momokotle.agent.presentation.dashboard;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider;

  private final Provider<UssdAutomationEngine> automationEngineProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  private final Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  private final Provider<SaveContactUseCase> saveContactUseCaseProvider;

  public DashboardViewModel_Factory(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    this.contextProvider = contextProvider;
    this.startUssdFlowUseCaseProvider = startUssdFlowUseCaseProvider;
    this.automationEngineProvider = automationEngineProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
    this.recordAttemptedTransactionUseCaseProvider = recordAttemptedTransactionUseCaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
    this.saveContactUseCaseProvider = saveContactUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(contextProvider.get(), startUssdFlowUseCaseProvider.get(), automationEngineProvider.get(), settingsDataStoreProvider.get(), recordAttemptedTransactionUseCaseProvider.get(), transactionRepositoryProvider.get(), savedContactRepositoryProvider.get(), saveContactUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Context> contextProvider,
      Provider<StartUssdFlowUseCase> startUssdFlowUseCaseProvider,
      Provider<UssdAutomationEngine> automationEngineProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider,
      Provider<RecordAttemptedTransactionUseCase> recordAttemptedTransactionUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    return new DashboardViewModel_Factory(contextProvider, startUssdFlowUseCaseProvider, automationEngineProvider, settingsDataStoreProvider, recordAttemptedTransactionUseCaseProvider, transactionRepositoryProvider, savedContactRepositoryProvider, saveContactUseCaseProvider);
  }

  public static DashboardViewModel newInstance(Context context,
      StartUssdFlowUseCase startUssdFlowUseCase, UssdAutomationEngine automationEngine,
      SettingsDataStore settingsDataStore,
      RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase,
      TransactionRepository transactionRepository, SavedContactRepository savedContactRepository,
      SaveContactUseCase saveContactUseCase) {
    return new DashboardViewModel(context, startUssdFlowUseCase, automationEngine, settingsDataStore, recordAttemptedTransactionUseCase, transactionRepository, savedContactRepository, saveContactUseCase);
  }
}
