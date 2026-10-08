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
public final class PurchaseSubscriptionUseCase_Factory implements Factory<PurchaseSubscriptionUseCase> {
  private final Provider<SubscriptionRepository> subscriptionRepositoryProvider;

  public PurchaseSubscriptionUseCase_Factory(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    this.subscriptionRepositoryProvider = subscriptionRepositoryProvider;
  }

  @Override
  public PurchaseSubscriptionUseCase get() {
    return newInstance(subscriptionRepositoryProvider.get());
  }

  public static PurchaseSubscriptionUseCase_Factory create(
      Provider<SubscriptionRepository> subscriptionRepositoryProvider) {
    return new PurchaseSubscriptionUseCase_Factory(subscriptionRepositoryProvider);
  }

  public static PurchaseSubscriptionUseCase newInstance(
      SubscriptionRepository subscriptionRepository) {
    return new PurchaseSubscriptionUseCase(subscriptionRepository);
  }
}
