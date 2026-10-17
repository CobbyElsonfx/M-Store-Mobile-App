package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.SavedContactRepository;
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
public final class PromoteFrequentCustomerContactUseCase_Factory implements Factory<PromoteFrequentCustomerContactUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  public PromoteFrequentCustomerContactUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
  }

  @Override
  public PromoteFrequentCustomerContactUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), savedContactRepositoryProvider.get());
  }

  public static PromoteFrequentCustomerContactUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    return new PromoteFrequentCustomerContactUseCase_Factory(transactionRepositoryProvider, savedContactRepositoryProvider);
  }

  public static PromoteFrequentCustomerContactUseCase newInstance(
      TransactionRepository transactionRepository, SavedContactRepository savedContactRepository) {
    return new PromoteFrequentCustomerContactUseCase(transactionRepository, savedContactRepository);
  }
}
