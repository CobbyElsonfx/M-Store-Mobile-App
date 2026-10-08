package com.momokotle.agent.presentation.referral;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.usecase.ObserveReferralInfoUseCase;
import com.momokotle.agent.domain.usecase.ObserveReferralsUseCase;
import com.momokotle.agent.domain.usecase.ObserveWalletTransactionsUseCase;
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
public final class ReferralViewModel_Factory implements Factory<ReferralViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ObserveReferralInfoUseCase> observeReferralInfoUseCaseProvider;

  private final Provider<ObserveReferralsUseCase> observeReferralsUseCaseProvider;

  private final Provider<ObserveWalletTransactionsUseCase> observeWalletTransactionsUseCaseProvider;

  public ReferralViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveReferralInfoUseCase> observeReferralInfoUseCaseProvider,
      Provider<ObserveReferralsUseCase> observeReferralsUseCaseProvider,
      Provider<ObserveWalletTransactionsUseCase> observeWalletTransactionsUseCaseProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.observeReferralInfoUseCaseProvider = observeReferralInfoUseCaseProvider;
    this.observeReferralsUseCaseProvider = observeReferralsUseCaseProvider;
    this.observeWalletTransactionsUseCaseProvider = observeWalletTransactionsUseCaseProvider;
  }

  @Override
  public ReferralViewModel get() {
    return newInstance(authRepositoryProvider.get(), observeReferralInfoUseCaseProvider.get(), observeReferralsUseCaseProvider.get(), observeWalletTransactionsUseCaseProvider.get());
  }

  public static ReferralViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveReferralInfoUseCase> observeReferralInfoUseCaseProvider,
      Provider<ObserveReferralsUseCase> observeReferralsUseCaseProvider,
      Provider<ObserveWalletTransactionsUseCase> observeWalletTransactionsUseCaseProvider) {
    return new ReferralViewModel_Factory(authRepositoryProvider, observeReferralInfoUseCaseProvider, observeReferralsUseCaseProvider, observeWalletTransactionsUseCaseProvider);
  }

  public static ReferralViewModel newInstance(AuthRepository authRepository,
      ObserveReferralInfoUseCase observeReferralInfoUseCase,
      ObserveReferralsUseCase observeReferralsUseCase,
      ObserveWalletTransactionsUseCase observeWalletTransactionsUseCase) {
    return new ReferralViewModel(authRepository, observeReferralInfoUseCase, observeReferralsUseCase, observeWalletTransactionsUseCase);
  }
}
