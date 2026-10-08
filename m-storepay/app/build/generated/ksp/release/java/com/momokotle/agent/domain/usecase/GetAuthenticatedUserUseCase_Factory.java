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
public final class GetAuthenticatedUserUseCase_Factory implements Factory<GetAuthenticatedUserUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public GetAuthenticatedUserUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public GetAuthenticatedUserUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static GetAuthenticatedUserUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new GetAuthenticatedUserUseCase_Factory(authRepositoryProvider);
  }

  public static GetAuthenticatedUserUseCase newInstance(AuthRepository authRepository) {
    return new GetAuthenticatedUserUseCase(authRepository);
  }
}
