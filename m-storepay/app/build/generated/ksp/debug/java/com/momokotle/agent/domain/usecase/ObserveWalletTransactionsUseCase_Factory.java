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
public final class ObserveWalletTransactionsUseCase_Factory implements Factory<ObserveWalletTransactionsUseCase> {
  private final Provider<WalletRepository> walletRepositoryProvider;

  public ObserveWalletTransactionsUseCase_Factory(
      Provider<WalletRepository> walletRepositoryProvider) {
    this.walletRepositoryProvider = walletRepositoryProvider;
  }

  @Override
  public ObserveWalletTransactionsUseCase get() {
    return newInstance(walletRepositoryProvider.get());
  }

  public static ObserveWalletTransactionsUseCase_Factory create(
      Provider<WalletRepository> walletRepositoryProvider) {
    return new ObserveWalletTransactionsUseCase_Factory(walletRepositoryProvider);
  }

  public static ObserveWalletTransactionsUseCase newInstance(WalletRepository walletRepository) {
    return new ObserveWalletTransactionsUseCase(walletRepository);
  }
}
