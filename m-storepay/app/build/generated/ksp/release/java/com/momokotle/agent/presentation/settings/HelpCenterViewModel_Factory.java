package com.momokotle.agent.presentation.settings;

import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase;
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
public final class HelpCenterViewModel_Factory implements Factory<HelpCenterViewModel> {
  private final Provider<ObserveBusinessSettingsUseCase> observeBusinessSettingsUseCaseProvider;

  public HelpCenterViewModel_Factory(
      Provider<ObserveBusinessSettingsUseCase> observeBusinessSettingsUseCaseProvider) {
    this.observeBusinessSettingsUseCaseProvider = observeBusinessSettingsUseCaseProvider;
  }

  @Override
  public HelpCenterViewModel get() {
    return newInstance(observeBusinessSettingsUseCaseProvider.get());
  }

  public static HelpCenterViewModel_Factory create(
      Provider<ObserveBusinessSettingsUseCase> observeBusinessSettingsUseCaseProvider) {
    return new HelpCenterViewModel_Factory(observeBusinessSettingsUseCaseProvider);
  }

  public static HelpCenterViewModel newInstance(
      ObserveBusinessSettingsUseCase observeBusinessSettingsUseCase) {
    return new HelpCenterViewModel(observeBusinessSettingsUseCase);
  }
}
