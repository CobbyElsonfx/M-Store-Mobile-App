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
public final class ResendRegistrationOtpUseCase_Factory implements Factory<ResendRegistrationOtpUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public ResendRegistrationOtpUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ResendRegistrationOtpUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static ResendRegistrationOtpUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new ResendRegistrationOtpUseCase_Factory(authRepositoryProvider);
  }

  public static ResendRegistrationOtpUseCase newInstance(AuthRepository authRepository) {
    return new ResendRegistrationOtpUseCase(authRepository);
  }
}
