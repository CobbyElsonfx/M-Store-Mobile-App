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
public final class RefreshUserProfileUseCase_Factory implements Factory<RefreshUserProfileUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public RefreshUserProfileUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public RefreshUserProfileUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static RefreshUserProfileUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new RefreshUserProfileUseCase_Factory(authRepositoryProvider);
  }

  public static RefreshUserProfileUseCase newInstance(AuthRepository authRepository) {
    return new RefreshUserProfileUseCase(authRepository);
  }
}
