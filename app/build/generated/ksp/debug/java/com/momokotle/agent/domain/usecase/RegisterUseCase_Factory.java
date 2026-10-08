package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.ReferralRepository;
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
public final class RegisterUseCase_Factory implements Factory<RegisterUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ReferralRepository> referralRepositoryProvider;

  public RegisterUseCase_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ReferralRepository> referralRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.referralRepositoryProvider = referralRepositoryProvider;
  }

  @Override
  public RegisterUseCase get() {
    return newInstance(authRepositoryProvider.get(), referralRepositoryProvider.get());
  }

  public static RegisterUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ReferralRepository> referralRepositoryProvider) {
    return new RegisterUseCase_Factory(authRepositoryProvider, referralRepositoryProvider);
  }

  public static RegisterUseCase newInstance(AuthRepository authRepository,
      ReferralRepository referralRepository) {
    return new RegisterUseCase(authRepository, referralRepository);
  }
}
