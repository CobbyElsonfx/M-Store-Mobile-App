package com.momokotle.agent.repository;

import com.momokotle.agent.domain.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class UserIdProviderImpl_Factory implements Factory<UserIdProviderImpl> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public UserIdProviderImpl_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public UserIdProviderImpl get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static UserIdProviderImpl_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new UserIdProviderImpl_Factory(authRepositoryProvider);
  }

  public static UserIdProviderImpl newInstance(AuthRepository authRepository) {
    return new UserIdProviderImpl(authRepository);
  }
}
