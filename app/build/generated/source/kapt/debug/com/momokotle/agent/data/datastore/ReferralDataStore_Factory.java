package com.momokotle.agent.data.datastore;

import android.content.Context;
import com.momokotle.agent.domain.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ReferralDataStore_Factory implements Factory<ReferralDataStore> {
  private final Provider<Context> contextProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public ReferralDataStore_Factory(Provider<Context> contextProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ReferralDataStore get() {
    return newInstance(contextProvider.get(), authRepositoryProvider.get());
  }

  public static ReferralDataStore_Factory create(Provider<Context> contextProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new ReferralDataStore_Factory(contextProvider, authRepositoryProvider);
  }

  public static ReferralDataStore newInstance(Context context, AuthRepository authRepository) {
    return new ReferralDataStore(context, authRepository);
  }
}
