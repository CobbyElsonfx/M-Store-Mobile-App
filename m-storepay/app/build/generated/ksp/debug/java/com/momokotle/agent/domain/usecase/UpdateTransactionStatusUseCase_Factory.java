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
public final class UpdateTransactionStatusUseCase_Factory implements Factory<UpdateTransactionStatusUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public UpdateTransactionStatusUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.calculateVendorProfitUseCaseProvider = calculateVendorProfitUseCaseProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public UpdateTransactionStatusUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), calculateVendorProfitUseCaseProvider.get(), settingsDataStoreProvider.get());
  }

  public static UpdateTransactionStatusUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CalculateVendorProfitUseCase> calculateVendorProfitUseCaseProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new UpdateTransactionStatusUseCase_Factory(transactionRepositoryProvider, calculateVendorProfitUseCaseProvider, settingsDataStoreProvider);
  }

  public static UpdateTransactionStatusUseCase newInstance(
      TransactionRepository transactionRepository,
      CalculateVendorProfitUseCase calculateVendorProfitUseCase,
      SettingsDataStore settingsDataStore) {
    return new UpdateTransactionStatusUseCase(transactionRepository, calculateVendorProfitUseCase, settingsDataStore);
  }
}
