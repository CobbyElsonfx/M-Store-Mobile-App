package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.TransactionRepository;
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
public final class RecordAttemptedTransactionUseCase_Factory implements Factory<RecordAttemptedTransactionUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public RecordAttemptedTransactionUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.calculateVendorProfitUseCaseProvider = calculateVendorProfitUseCaseProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public RecordAttemptedTransactionUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), calculateVendorProfitUseCaseProvider.get(), settingsDataStoreProvider.get());
  }

  public static RecordAttemptedTransactionUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new RecordAttemptedTransactionUseCase_Factory(transactionRepositoryProvider, calculateVendorProfitUseCaseProvider, settingsDataStoreProvider);
  }

  public static RecordAttemptedTransactionUseCase newInstance(
      TransactionRepository transactionRepository,
      CalculateVendorProfitUseCase calculateVendorProfitUseCase,
      SettingsDataStore settingsDataStore) {
    return new RecordAttemptedTransactionUseCase(transactionRepository, calculateVendorProfitUseCase, settingsDataStore);
  }
}
