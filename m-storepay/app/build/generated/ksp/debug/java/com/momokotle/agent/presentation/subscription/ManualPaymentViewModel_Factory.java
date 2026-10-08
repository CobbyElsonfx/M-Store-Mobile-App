package com.momokotle.agent.presentation.subscription;

import androidx.lifecycle.SavedStateHandle;
import com.momokotle.agent.domain.usecase.CheckSubscriptionPaymentStatusUseCase;
import com.momokotle.agent.domain.usecase.InitializeManualSubscriptionPaymentUseCase;
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase;
import com.momokotle.agent.domain.usecase.ObserveSubscriptionPlansUseCase;
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase;
import com.momokotle.agent.utils.NetworkMonitor;
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
public final class ManualPaymentViewModel_Factory implements Factory<ManualPaymentViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider;

  private final Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider;

  private final Provider<InitializeManualSubscriptionPaymentUseCase> initializeManualPaymentUseCaseProvider;

  private final Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider;

  private final Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public ManualPaymentViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<InitializeManualSubscriptionPaymentUseCase> initializeManualPaymentUseCaseProvider,
      Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.observePlansUseCaseProvider = observePlansUseCaseProvider;
    this.observeSettingsUseCaseProvider = observeSettingsUseCaseProvider;
    this.initializeManualPaymentUseCaseProvider = initializeManualPaymentUseCaseProvider;
    this.checkPaymentStatusUseCaseProvider = checkPaymentStatusUseCaseProvider;
    this.refreshUserProfileUseCaseProvider = refreshUserProfileUseCaseProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public ManualPaymentViewModel get() {
    return newInstance(savedStateHandleProvider.get(), observePlansUseCaseProvider.get(), observeSettingsUseCaseProvider.get(), initializeManualPaymentUseCaseProvider.get(), checkPaymentStatusUseCaseProvider.get(), refreshUserProfileUseCaseProvider.get(), networkMonitorProvider.get());
  }

  public static ManualPaymentViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<InitializeManualSubscriptionPaymentUseCase> initializeManualPaymentUseCaseProvider,
      Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    return new ManualPaymentViewModel_Factory(savedStateHandleProvider, observePlansUseCaseProvider, observeSettingsUseCaseProvider, initializeManualPaymentUseCaseProvider, checkPaymentStatusUseCaseProvider, refreshUserProfileUseCaseProvider, networkMonitorProvider);
  }

  public static ManualPaymentViewModel newInstance(SavedStateHandle savedStateHandle,
      ObserveSubscriptionPlansUseCase observePlansUseCase,
      ObserveBusinessSettingsUseCase observeSettingsUseCase,
      InitializeManualSubscriptionPaymentUseCase initializeManualPaymentUseCase,
      CheckSubscriptionPaymentStatusUseCase checkPaymentStatusUseCase,
      RefreshUserProfileUseCase refreshUserProfileUseCase, NetworkMonitor networkMonitor) {
    return new ManualPaymentViewModel(savedStateHandle, observePlansUseCase, observeSettingsUseCase, initializeManualPaymentUseCase, checkPaymentStatusUseCase, refreshUserProfileUseCase, networkMonitor);
  }
}
