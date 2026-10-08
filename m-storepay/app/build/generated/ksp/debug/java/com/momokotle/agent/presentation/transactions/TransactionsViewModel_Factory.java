package com.momokotle.agent.presentation.transactions;

import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.domain.usecase.ObserveCommissionSummaryUseCase;
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
public final class TransactionsViewModel_Factory implements Factory<TransactionsViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider;

  public TransactionsViewModel_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.observeCommissionSummaryUseCaseProvider = observeCommissionSummaryUseCaseProvider;
  }

  @Override
  public TransactionsViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), observeCommissionSummaryUseCaseProvider.get());
  }

  public static TransactionsViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<ObserveCommissionSummaryUseCase> observeCommissionSummaryUseCaseProvider) {
    return new TransactionsViewModel_Factory(transactionRepositoryProvider, observeCommissionSummaryUseCaseProvider);
  }

  public static TransactionsViewModel newInstance(TransactionRepository transactionRepository,
      ObserveCommissionSummaryUseCase observeCommissionSummaryUseCase) {
    return new TransactionsViewModel(transactionRepository, observeCommissionSummaryUseCase);
  }
}
