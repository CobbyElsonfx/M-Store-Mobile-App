package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.BusinessSettingsRepository;
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
public final class ObserveBusinessSettingsUseCase_Factory implements Factory<ObserveBusinessSettingsUseCase> {
  private final Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider;

  public ObserveBusinessSettingsUseCase_Factory(
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider) {
    this.businessSettingsRepositoryProvider = businessSettingsRepositoryProvider;
  }

  @Override
  public ObserveBusinessSettingsUseCase get() {
    return newInstance(businessSettingsRepositoryProvider.get());
  }

  public static ObserveBusinessSettingsUseCase_Factory create(
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider) {
    return new ObserveBusinessSettingsUseCase_Factory(businessSettingsRepositoryProvider);
  }

  public static ObserveBusinessSettingsUseCase newInstance(
      BusinessSettingsRepository businessSettingsRepository) {
    return new ObserveBusinessSettingsUseCase(businessSettingsRepository);
  }
}
