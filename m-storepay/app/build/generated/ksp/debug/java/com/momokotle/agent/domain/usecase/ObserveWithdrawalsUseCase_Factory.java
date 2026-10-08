package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.WalletRepository;
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
public final class ObserveWithdrawalsUseCase_Factory implements Factory<ObserveWithdrawalsUseCase> {
  private final Provider<WalletRepository> walletRepositoryProvider;

  public ObserveWithdrawalsUseCase_Factory(Provider<WalletRepository> walletRepositoryProvider) {
    this.walletRepositoryProvider = walletRepositoryProvider;
  }

  @Override
  public ObserveWithdrawalsUseCase get() {
    return newInstance(walletRepositoryProvider.get());
  }

  public static ObserveWithdrawalsUseCase_Factory create(
      Provider<WalletRepository> walletRepositoryProvider) {
    return new ObserveWithdrawalsUseCase_Factory(walletRepositoryProvider);
  }

  public static ObserveWithdrawalsUseCase newInstance(WalletRepository walletRepository) {
    return new ObserveWithdrawalsUseCase(walletRepository);
  }
}
