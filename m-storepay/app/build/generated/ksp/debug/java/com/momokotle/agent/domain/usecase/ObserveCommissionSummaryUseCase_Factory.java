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
public final class ObserveCommissionSummaryUseCase_Factory implements Factory<ObserveCommissionSummaryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public ObserveCommissionSummaryUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public ObserveCommissionSummaryUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static ObserveCommissionSummaryUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new ObserveCommissionSummaryUseCase_Factory(transactionRepositoryProvider);
  }

  public static ObserveCommissionSummaryUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new ObserveCommissionSummaryUseCase(transactionRepository);
  }
}
