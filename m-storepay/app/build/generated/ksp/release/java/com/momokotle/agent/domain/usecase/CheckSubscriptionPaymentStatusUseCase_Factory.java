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
public final class CheckSubscriptionPaymentStatusUseCase_Factory implements Factory<CheckSubscriptionPaymentStatusUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public CheckSubscriptionPaymentStatusUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public CheckSubscriptionPaymentStatusUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static CheckSubscriptionPaymentStatusUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new CheckSubscriptionPaymentStatusUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static CheckSubscriptionPaymentStatusUseCase newInstance(
      SubscriptionRepository subscriptionRepository) {
    return new CheckSubscriptionPaymentStatusUseCase(subscriptionRepository);
  }
}
