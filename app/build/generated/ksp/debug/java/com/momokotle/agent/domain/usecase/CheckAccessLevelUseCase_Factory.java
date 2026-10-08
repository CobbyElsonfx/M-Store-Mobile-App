package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.SubscriptionRepository;
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
public final class CheckAccessLevelUseCase_Factory implements Factory<CheckAccessLevelUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public CheckAccessLevelUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public CheckAccessLevelUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static CheckAccessLevelUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new CheckAccessLevelUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static CheckAccessLevelUseCase newInstance(SubscriptionRepository subscriptionRepository) {
    return new CheckAccessLevelUseCase(subscriptionRepository);
  }
}
