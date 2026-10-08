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
public final class ObserveWalletUseCase_Factory implements Factory<ObserveWalletUseCase> {
  private final Provider<WalletRepository> walletRepositoryProvider;

  public ObserveWalletUseCase_Factory(Provider<WalletRepository> walletRepositoryProvider) {
    this.walletRepositoryProvider = walletRepositoryProvider;
  }

  @Override
  public ObserveWalletUseCase get() {
    return newInstance(walletRepositoryProvider.get());
  }

  public static ObserveWalletUseCase_Factory create(
      Provider<WalletRepository> walletRepositoryProvider) {
    return new ObserveWalletUseCase_Factory(walletRepositoryProvider);
  }

  public static ObserveWalletUseCase newInstance(WalletRepository walletRepository) {
    return new ObserveWalletUseCase(walletRepository);
  }
}
