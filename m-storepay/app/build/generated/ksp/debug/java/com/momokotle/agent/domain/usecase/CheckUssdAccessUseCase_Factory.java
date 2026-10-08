package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.domain.ussd.EntitlementClock;
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
public final class CheckUssdAccessUseCase_Factory implements Factory<CheckUssdAccessUseCase> {
  private final Provider<UserAccountDataStore> userAccountDataStoreProvider;

  private final Provider<EntitlementClock> clockProvider;

  public CheckUssdAccessUseCase_Factory(Provider<UserAccountDataStore> userAccountDataStoreProvider,
      Provider<EntitlementClock> clockProvider) {
    this.userAccountDataStoreProvider = userAccountDataStoreProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public CheckUssdAccessUseCase get() {
    return newInstance(userAccountDataStoreProvider.get(), clockProvider.get());
  }

  public static CheckUssdAccessUseCase_Factory create(
      Provider<UserAccountDataStore> userAccountDataStoreProvider,
      Provider<EntitlementClock> clockProvider) {
    return new CheckUssdAccessUseCase_Factory(userAccountDataStoreProvider, clockProvider);
  }

  public static CheckUssdAccessUseCase newInstance(UserAccountDataStore userAccountDataStore,
      EntitlementClock clock) {
    return new CheckUssdAccessUseCase(userAccountDataStore, clock);
  }
}
