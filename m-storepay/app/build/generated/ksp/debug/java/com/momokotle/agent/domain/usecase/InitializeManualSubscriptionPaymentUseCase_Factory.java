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
public final class InitializeManualSubscriptionPaymentUseCase_Factory implements Factory<InitializeManualSubscriptionPaymentUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public InitializeManualSubscriptionPaymentUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public InitializeManualSubscriptionPaymentUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static InitializeManualSubscriptionPaymentUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new InitializeManualSubscriptionPaymentUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static InitializeManualSubscriptionPaymentUseCase newInstance(
      SubscriptionRepository subscriptionRepository) {
    return new InitializeManualSubscriptionPaymentUseCase(subscriptionRepository);
  }
}
