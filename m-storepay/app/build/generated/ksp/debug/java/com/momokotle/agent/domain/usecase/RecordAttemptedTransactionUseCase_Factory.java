package com.momokotle.agent.domain.usecase;

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

  public RecordAttemptedTransactionUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public RecordAttemptedTransactionUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static RecordAttemptedTransactionUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new RecordAttemptedTransactionUseCase_Factory(transactionRepositoryProvider);
  }

  public static RecordAttemptedTransactionUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new RecordAttemptedTransactionUseCase(transactionRepository);
  }
}
