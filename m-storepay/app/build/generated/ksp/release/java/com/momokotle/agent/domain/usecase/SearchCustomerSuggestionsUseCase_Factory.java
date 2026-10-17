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
public final class SearchCustomerSuggestionsUseCase_Factory implements Factory<SearchCustomerSuggestionsUseCase> {
  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public SearchCustomerSuggestionsUseCase_Factory(
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public SearchCustomerSuggestionsUseCase get() {
    return newInstance(savedContactRepositoryProvider.get(), transactionRepositoryProvider.get());
  }

  public static SearchCustomerSuggestionsUseCase_Factory create(
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new SearchCustomerSuggestionsUseCase_Factory(savedContactRepositoryProvider, transactionRepositoryProvider);
  }

  public static SearchCustomerSuggestionsUseCase newInstance(
      SavedContactRepository savedContactRepository, TransactionRepository transactionRepository) {
    return new SearchCustomerSuggestionsUseCase(savedContactRepository, transactionRepository);
  }
}
