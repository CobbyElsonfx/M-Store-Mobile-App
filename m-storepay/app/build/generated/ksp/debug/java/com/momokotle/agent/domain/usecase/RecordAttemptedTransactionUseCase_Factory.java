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

  private final Provider<PromoteFrequentCustomerContactUseCase> promoteFrequentCustomerContactUseCaseProvider;

  public RecordAttemptedTransactionUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<PromoteFrequentCustomerContactUseCase> promoteFrequentCustomerContactUseCaseProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.promoteFrequentCustomerContactUseCaseProvider = promoteFrequentCustomerContactUseCaseProvider;
  }

  @Override
  public RecordAttemptedTransactionUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), promoteFrequentCustomerContactUseCaseProvider.get());
  }

  public static RecordAttemptedTransactionUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<PromoteFrequentCustomerContactUseCase> promoteFrequentCustomerContactUseCaseProvider) {
    return new RecordAttemptedTransactionUseCase_Factory(transactionRepositoryProvider, promoteFrequentCustomerContactUseCaseProvider);
  }

  public static RecordAttemptedTransactionUseCase newInstance(
      TransactionRepository transactionRepository,
      PromoteFrequentCustomerContactUseCase promoteFrequentCustomerContactUseCase) {
    return new RecordAttemptedTransactionUseCase(transactionRepository, promoteFrequentCustomerContactUseCase);
  }
}
