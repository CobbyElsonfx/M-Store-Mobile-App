package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.SavedContactRepository;
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
public final class SaveTransactionContactUseCase_Factory implements Factory<SaveTransactionContactUseCase> {
  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  public SaveTransactionContactUseCase_Factory(
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
  }

  @Override
  public SaveTransactionContactUseCase get() {
    return newInstance(savedContactRepositoryProvider.get());
  }

  public static SaveTransactionContactUseCase_Factory create(
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    return new SaveTransactionContactUseCase_Factory(savedContactRepositoryProvider);
  }

  public static SaveTransactionContactUseCase newInstance(
      SavedContactRepository savedContactRepository) {
    return new SaveTransactionContactUseCase(savedContactRepository);
  }
}
