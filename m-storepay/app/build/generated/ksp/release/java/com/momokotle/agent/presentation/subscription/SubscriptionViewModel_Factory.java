package com.momokotle.agent.presentation.subscription;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.usecase.CheckSubscriptionPaymentStatusUseCase;
import com.momokotle.agent.domain.usecase.InitializeSubscriptionPaymentUseCase;
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
public final class SubscriptionViewModel_Factory implements Factory<SubscriptionViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider;

  private final Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider;

  private final Provider<InitializeSubscriptionPaymentUseCase> initializePaymentUseCaseProvider;

  private final Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider;

  private final Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public SubscriptionViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<InitializeSubscriptionPaymentUseCase> initializePaymentUseCaseProvider,
      Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.observePlansUseCaseProvider = observePlansUseCaseProvider;
    this.observeSettingsUseCaseProvider = observeSettingsUseCaseProvider;
    this.initializePaymentUseCaseProvider = initializePaymentUseCaseProvider;
    this.checkPaymentStatusUseCaseProvider = checkPaymentStatusUseCaseProvider;
    this.refreshUserProfileUseCaseProvider = refreshUserProfileUseCaseProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public SubscriptionViewModel get() {
    return newInstance(authRepositoryProvider.get(), observePlansUseCaseProvider.get(), observeSettingsUseCaseProvider.get(), initializePaymentUseCaseProvider.get(), checkPaymentStatusUseCaseProvider.get(), refreshUserProfileUseCaseProvider.get(), networkMonitorProvider.get());
  }

  public static SubscriptionViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<InitializeSubscriptionPaymentUseCase> initializePaymentUseCaseProvider,
      Provider<CheckSubscriptionPaymentStatusUseCase> checkPaymentStatusUseCaseProvider,
      Provider<RefreshUserProfileUseCase> refreshUserProfileUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    return new SubscriptionViewModel_Factory(authRepositoryProvider, observePlansUseCaseProvider, observeSettingsUseCaseProvider, initializePaymentUseCaseProvider, checkPaymentStatusUseCaseProvider, refreshUserProfileUseCaseProvider, networkMonitorProvider);
  }

  public static SubscriptionViewModel newInstance(AuthRepository authRepository,
      ObserveSubscriptionPlansUseCase observePlansUseCase,
      ObserveBusinessSettingsUseCase observeSettingsUseCase,
      InitializeSubscriptionPaymentUseCase initializePaymentUseCase,
      CheckSubscriptionPaymentStatusUseCase checkPaymentStatusUseCase,
      RefreshUserProfileUseCase refreshUserProfileUseCase, NetworkMonitor networkMonitor) {
    return new SubscriptionViewModel(authRepository, observePlansUseCase, observeSettingsUseCase, initializePaymentUseCase, checkPaymentStatusUseCase, refreshUserProfileUseCase, networkMonitor);
  }
}
