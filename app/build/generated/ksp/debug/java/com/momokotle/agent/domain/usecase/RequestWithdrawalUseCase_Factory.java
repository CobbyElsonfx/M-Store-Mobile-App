package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.BusinessSettingsRepository;
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
public final class RequestWithdrawalUseCase_Factory implements Factory<RequestWithdrawalUseCase> {
  private final Provider<WalletRepository> walletRepositoryProvider;

  private final Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider;

  public RequestWithdrawalUseCase_Factory(Provider<WalletRepository> walletRepositoryProvider,
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider) {
    this.walletRepositoryProvider = walletRepositoryProvider;
    this.businessSettingsRepositoryProvider = businessSettingsRepositoryProvider;
  }

  @Override
  public RequestWithdrawalUseCase get() {
    return newInstance(walletRepositoryProvider.get(), businessSettingsRepositoryProvider.get());
  }

  public static RequestWithdrawalUseCase_Factory create(
      Provider<WalletRepository> walletRepositoryProvider,
      Provider<BusinessSettingsRepository> businessSettingsRepositoryProvider) {
    return new RequestWithdrawalUseCase_Factory(walletRepositoryProvider, businessSettingsRepositoryProvider);
  }

  public static RequestWithdrawalUseCase newInstance(WalletRepository walletRepository,
      BusinessSettingsRepository businessSettingsRepository) {
    return new RequestWithdrawalUseCase(walletRepository, businessSettingsRepository);
  }
}
