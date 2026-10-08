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
public final class CheckDuplicateTransactionUseCase_Factory implements Factory<CheckDuplicateTransactionUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<SettingsDataStore> settingsDataStoreProvider;

  public CheckDuplicateTransactionUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.settingsDataStoreProvider = settingsDataStoreProvider;
  }

  @Override
  public CheckDuplicateTransactionUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), settingsDataStoreProvider.get());
  }

  public static CheckDuplicateTransactionUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SettingsDataStore> settingsDataStoreProvider) {
    return new CheckDuplicateTransactionUseCase_Factory(transactionRepositoryProvider, settingsDataStoreProvider);
  }

  public static CheckDuplicateTransactionUseCase newInstance(
      TransactionRepository transactionRepository, SettingsDataStore settingsDataStore) {
    return new CheckDuplicateTransactionUseCase(transactionRepository, settingsDataStore);
  }
}
