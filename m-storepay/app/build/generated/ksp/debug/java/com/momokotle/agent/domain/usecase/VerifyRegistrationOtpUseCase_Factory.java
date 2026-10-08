package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.AuthRepository;
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
public final class VerifyRegistrationOtpUseCase_Factory implements Factory<VerifyRegistrationOtpUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public VerifyRegistrationOtpUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public VerifyRegistrationOtpUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static VerifyRegistrationOtpUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new VerifyRegistrationOtpUseCase_Factory(authRepositoryProvider);
  }

  public static VerifyRegistrationOtpUseCase newInstance(AuthRepository authRepository) {
    return new VerifyRegistrationOtpUseCase(authRepository);
  }
}
