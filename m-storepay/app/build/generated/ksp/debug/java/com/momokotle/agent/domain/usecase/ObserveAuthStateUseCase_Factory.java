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
public final class ObserveAuthStateUseCase_Factory implements Factory<ObserveAuthStateUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public ObserveAuthStateUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ObserveAuthStateUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static ObserveAuthStateUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider) {
    return new ObserveAuthStateUseCase_Factory(authRepositoryProvider);
  }

  public static ObserveAuthStateUseCase newInstance(AuthRepository authRepository) {
    return new ObserveAuthStateUseCase(authRepository);
  }
}
