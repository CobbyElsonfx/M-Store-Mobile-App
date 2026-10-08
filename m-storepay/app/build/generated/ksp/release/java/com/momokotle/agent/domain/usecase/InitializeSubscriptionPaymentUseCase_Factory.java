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
public final class InitializeSubscriptionPaymentUseCase_Factory implements Factory<InitializeSubscriptionPaymentUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public InitializeSubscriptionPaymentUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public InitializeSubscriptionPaymentUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static InitializeSubscriptionPaymentUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new InitializeSubscriptionPaymentUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static InitializeSubscriptionPaymentUseCase newInstance(
      SubscriptionRepository subscriptionRepository) {
    return new InitializeSubscriptionPaymentUseCase(subscriptionRepository);
  }
}
