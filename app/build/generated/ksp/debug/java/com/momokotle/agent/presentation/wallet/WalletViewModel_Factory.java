package com.momokotle.agent.presentation.wallet;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.usecase.ObserveWalletTransactionsUseCase;
import com.momokotle.agent.domain.usecase.ObserveWalletUseCase;
import com.momokotle.agent.domain.usecase.ObserveWithdrawalsUseCase;
import com.momokotle.agent.domain.usecase.RequestWithdrawalUseCase;
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
public final class WalletViewModel_Factory implements Factory<WalletViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ObserveWalletUseCase> observeWalletUseCaseProvider;

  private final Provider<ObserveWalletTransactionsUseCase> observeTransactionsUseCaseProvider;

  private final Provider<ObserveWithdrawalsUseCase> observeWithdrawalsUseCaseProvider;

  private final Provider<RequestWithdrawalUseCase> requestWithdrawalUseCaseProvider;

  public WalletViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveWalletUseCase> observeWalletUseCaseProvider,
      Provider<ObserveWalletTransactionsUseCase> observeTransactionsUseCaseProvider,
      Provider<ObserveWithdrawalsUseCase> observeWithdrawalsUseCaseProvider,
      Provider<RequestWithdrawalUseCase> requestWithdrawalUseCaseProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.observeWalletUseCaseProvider = observeWalletUseCaseProvider;
    this.observeTransactionsUseCaseProvider = observeTransactionsUseCaseProvider;
    this.observeWithdrawalsUseCaseProvider = observeWithdrawalsUseCaseProvider;
    this.requestWithdrawalUseCaseProvider = requestWithdrawalUseCaseProvider;
  }

  @Override
  public WalletViewModel get() {
    return newInstance(authRepositoryProvider.get(), observeWalletUseCaseProvider.get(), observeTransactionsUseCaseProvider.get(), observeWithdrawalsUseCaseProvider.get(), requestWithdrawalUseCaseProvider.get());
  }

  public static WalletViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveWalletUseCase> observeWalletUseCaseProvider,
      Provider<ObserveWalletTransactionsUseCase> observeTransactionsUseCaseProvider,
      Provider<ObserveWithdrawalsUseCase> observeWithdrawalsUseCaseProvider,
      Provider<RequestWithdrawalUseCase> requestWithdrawalUseCaseProvider) {
    return new WalletViewModel_Factory(authRepositoryProvider, observeWalletUseCaseProvider, observeTransactionsUseCaseProvider, observeWithdrawalsUseCaseProvider, requestWithdrawalUseCaseProvider);
  }

  public static WalletViewModel newInstance(AuthRepository authRepository,
      ObserveWalletUseCase observeWalletUseCase,
      ObserveWalletTransactionsUseCase observeTransactionsUseCase,
      ObserveWithdrawalsUseCase observeWithdrawalsUseCase,
      RequestWithdrawalUseCase requestWithdrawalUseCase) {
    return new WalletViewModel(authRepository, observeWalletUseCase, observeTransactionsUseCase, observeWithdrawalsUseCase, requestWithdrawalUseCase);
  }
}
