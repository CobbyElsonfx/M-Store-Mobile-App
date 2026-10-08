package com.momokotle.agent.presentation.subscription;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase;
import com.momokotle.agent.domain.usecase.ObserveSubscriptionPlansUseCase;
import com.momokotle.agent.domain.usecase.PurchaseSubscriptionUseCase;
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

  private final Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider;

  public SubscriptionViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.observePlansUseCaseProvider = observePlansUseCaseProvider;
    this.observeSettingsUseCaseProvider = observeSettingsUseCaseProvider;
    this.purchaseSubscriptionUseCaseProvider = purchaseSubscriptionUseCaseProvider;
  }

  @Override
  public SubscriptionViewModel get() {
    return newInstance(authRepositoryProvider.get(), observePlansUseCaseProvider.get(), observeSettingsUseCaseProvider.get(), purchaseSubscriptionUseCaseProvider.get());
  }

  public static SubscriptionViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveSubscriptionPlansUseCase> observePlansUseCaseProvider,
      Provider<ObserveBusinessSettingsUseCase> observeSettingsUseCaseProvider,
      Provider<PurchaseSubscriptionUseCase> purchaseSubscriptionUseCaseProvider) {
    return new SubscriptionViewModel_Factory(authRepositoryProvider, observePlansUseCaseProvider, observeSettingsUseCaseProvider, purchaseSubscriptionUseCaseProvider);
  }

  public static SubscriptionViewModel newInstance(AuthRepository authRepository,
      ObserveSubscriptionPlansUseCase observePlansUseCase,
      ObserveBusinessSettingsUseCase observeSettingsUseCase,
      PurchaseSubscriptionUseCase purchaseSubscriptionUseCase) {
    return new SubscriptionViewModel(authRepository, observePlansUseCase, observeSettingsUseCase, purchaseSubscriptionUseCase);
  }
}
