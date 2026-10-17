package com.momokotle.agent.presentation.contacts;

import com.momokotle.agent.data.contacts.DeviceContactReader;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.usecase.ImportDeviceContactsUseCase;
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

  private final Provider<DeviceContactReader> deviceContactReaderProvider;

  private final Provider<ImportDeviceContactsUseCase> importDeviceContactsUseCaseProvider;

  public ContactsViewModel_Factory(Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider,
      Provider<DeviceContactReader> deviceContactReaderProvider,
      Provider<ImportDeviceContactsUseCase> importDeviceContactsUseCaseProvider) {
    this.savedContactRepositoryProvider = savedContactRepositoryProvider;
    this.saveContactUseCaseProvider = saveContactUseCaseProvider;
    this.deviceContactReaderProvider = deviceContactReaderProvider;
    this.importDeviceContactsUseCaseProvider = importDeviceContactsUseCaseProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(savedContactRepositoryProvider.get(), saveContactUseCaseProvider.get(), deviceContactReaderProvider.get(), importDeviceContactsUseCaseProvider.get());
  }

  public static ContactsViewModel_Factory create(
      Provider<SavedContactRepository> savedContactRepositoryProvider,
      Provider<SaveContactUseCase> saveContactUseCaseProvider,
      Provider<DeviceContactReader> deviceContactReaderProvider,
      Provider<ImportDeviceContactsUseCase> importDeviceContactsUseCaseProvider) {
    return new ContactsViewModel_Factory(savedContactRepositoryProvider, saveContactUseCaseProvider, deviceContactReaderProvider, importDeviceContactsUseCaseProvider);
  }

  public static ContactsViewModel newInstance(SavedContactRepository savedContactRepository,
      SaveContactUseCase saveContactUseCase, DeviceContactReader deviceContactReader,
      ImportDeviceContactsUseCase importDeviceContactsUseCase) {
    return new ContactsViewModel(savedContactRepository, saveContactUseCase, deviceContactReader, importDeviceContactsUseCase);
  }
}
