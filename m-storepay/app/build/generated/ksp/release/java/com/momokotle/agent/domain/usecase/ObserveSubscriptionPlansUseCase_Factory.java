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
public final class ObserveSubscriptionPlansUseCase_Factory implements Factory<ObserveSubscriptionPlansUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public ObserveSubscriptionPlansUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public ObserveSubscriptionPlansUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static ObserveSubscriptionPlansUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new ObserveSubscriptionPlansUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static ObserveSubscriptionPlansUseCase newInstance(
      SubscriptionRepository subscriptionRepository) {
    return new ObserveSubscriptionPlansUseCase(subscriptionRepository);
  }
}
