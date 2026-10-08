package com.momokotle.agent.presentation.auth;

import com.momokotle.agent.domain.usecase.RegisterUseCase;
import com.momokotle.agent.domain.usecase.ResendRegistrationOtpUseCase;
import com.momokotle.agent.domain.usecase.VerifyRegistrationOtpUseCase;
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
public final class RegisterViewModel_Factory implements Factory<RegisterViewModel> {
  private final Provider<RegisterUseCase> registerUseCaseProvider;

  private final Provider<VerifyRegistrationOtpUseCase> verifyRegistrationOtpUseCaseProvider;

  private final Provider<ResendRegistrationOtpUseCase> resendRegistrationOtpUseCaseProvider;

  public RegisterViewModel_Factory(Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<VerifyRegistrationOtpUseCase> verifyRegistrationOtpUseCaseProvider,
      Provider<ResendRegistrationOtpUseCase> resendRegistrationOtpUseCaseProvider) {
    this.registerUseCaseProvider = registerUseCaseProvider;
    this.verifyRegistrationOtpUseCaseProvider = verifyRegistrationOtpUseCaseProvider;
    this.resendRegistrationOtpUseCaseProvider = resendRegistrationOtpUseCaseProvider;
  }

  @Override
  public RegisterViewModel get() {
    return newInstance(registerUseCaseProvider.get(), verifyRegistrationOtpUseCaseProvider.get(), resendRegistrationOtpUseCaseProvider.get());
  }

  public static RegisterViewModel_Factory create(Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<VerifyRegistrationOtpUseCase> verifyRegistrationOtpUseCaseProvider,
      Provider<ResendRegistrationOtpUseCase> resendRegistrationOtpUseCaseProvider) {
    return new RegisterViewModel_Factory(registerUseCaseProvider, verifyRegistrationOtpUseCaseProvider, resendRegistrationOtpUseCaseProvider);
  }

  public static RegisterViewModel newInstance(RegisterUseCase registerUseCase,
      VerifyRegistrationOtpUseCase verifyRegistrationOtpUseCase,
      ResendRegistrationOtpUseCase resendRegistrationOtpUseCase) {
    return new RegisterViewModel(registerUseCase, verifyRegistrationOtpUseCase, resendRegistrationOtpUseCase);
  }
}
