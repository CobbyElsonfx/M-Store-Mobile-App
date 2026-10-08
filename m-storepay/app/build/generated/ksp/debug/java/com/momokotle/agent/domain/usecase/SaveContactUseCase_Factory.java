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
public final class SaveContactUseCase_Factory implements Factory<SaveContactUseCase> {
  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  public SaveContactUseCase_Factory(
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
  }

  @Override
  public SaveContactUseCase get() {
    return newInstance(savedContactRepositoryProvider.get());
  }

  public static SaveContactUseCase_Factory create(
      Provider<SavedContactRepository> savedContactRepositoryProvider) {
    return new SaveContactUseCase_Factory(savedContactRepositoryProvider);
  }

  public static SaveContactUseCase newInstance(SavedContactRepository savedContactRepository) {
    return new SaveContactUseCase(savedContactRepository);
  }
}
