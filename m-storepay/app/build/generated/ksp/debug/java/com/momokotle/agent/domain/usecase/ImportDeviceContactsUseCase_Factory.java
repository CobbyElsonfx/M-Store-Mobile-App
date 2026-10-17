package com.momokotle.agent.domain.usecase;

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
public final class ImportDeviceContactsUseCase_Factory implements Factory<ImportDeviceContactsUseCase> {
  private final Provider<SaveContactUseCase> saveContactUseCaseProvider;

  public ImportDeviceContactsUseCase_Factory(
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    this.saveContactUseCaseProvider = saveContactUseCaseProvider;
  }

  @Override
  public ImportDeviceContactsUseCase get() {
    return newInstance(saveContactUseCaseProvider.get());
  }

  public static ImportDeviceContactsUseCase_Factory create(
      Provider<SaveContactUseCase> saveContactUseCaseProvider) {
    return new ImportDeviceContactsUseCase_Factory(saveContactUseCaseProvider);
  }

  public static ImportDeviceContactsUseCase newInstance(SaveContactUseCase saveContactUseCase) {
    return new ImportDeviceContactsUseCase(saveContactUseCase);
  }
}
