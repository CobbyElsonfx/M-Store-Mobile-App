package com.momokotle.agent.presentation.contacts;

import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
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
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<SavedContactRepository> savedContactRepositoryProvider;

  private final Provider<SaveContactUseCase> saveContactUseCaseProvider;

  public ContactsViewModel_Factory(Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
    this.saveContactUseCaseProvider = saveContactUseCaseProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(savedContactRepositoryProvider.get(), saveContactUseCaseProvider.get());
  }

  public static ContactsViewModel_Factory create(
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    return new ContactsViewModel_Factory(savedContactRepositoryProvider, saveContactUseCaseProvider);
  }

  public static ContactsViewModel newInstance(SavedContactRepository savedContactRepository,
      SaveContactUseCase saveContactUseCase) {
    return new ContactsViewModel(savedContactRepository, saveContactUseCase);
  }
}
