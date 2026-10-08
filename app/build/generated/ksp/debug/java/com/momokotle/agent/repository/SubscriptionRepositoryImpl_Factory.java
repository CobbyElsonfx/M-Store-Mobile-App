package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelSubscriptionPlanRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelSubscriptionRemoteDataSource;
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
public final class SubscriptionRepositoryImpl_Factory implements Factory<SubscriptionRepositoryImpl> {
  private final Provider<LaravelSubscriptionPlanRemoteDataSource> planRemoteDataSourceProvider;

  private final Provider<LaravelSubscriptionRemoteDataSource> subscriptionRemoteDataSourceProvider;

  public SubscriptionRepositoryImpl_Factory(
      Provider<LaravelSubscriptionPlanRemoteDataSource> planRemoteDataSourceProvider,
      Provider<LaravelSubscriptionRemoteDataSource> subscriptionRemoteDataSourceProvider) {
    this.planRemoteDataSourceProvider = planRemoteDataSourceProvider;
    this.subscriptionRemoteDataSourceProvider = subscriptionRemoteDataSourceProvider;
  }

  @Override
  public SubscriptionRepositoryImpl get() {
    return newInstance(planRemoteDataSourceProvider.get(), subscriptionRemoteDataSourceProvider.get());
  }

  public static SubscriptionRepositoryImpl_Factory create(
      Provider<LaravelSubscriptionPlanRemoteDataSource> planRemoteDataSourceProvider,
      Provider<LaravelSubscriptionRemoteDataSource> subscriptionRemoteDataSourceProvider) {
    return new SubscriptionRepositoryImpl_Factory(planRemoteDataSourceProvider, subscriptionRemoteDataSourceProvider);
  }

  public static SubscriptionRepositoryImpl newInstance(
      LaravelSubscriptionPlanRemoteDataSource planRemoteDataSource,
      LaravelSubscriptionRemoteDataSource subscriptionRemoteDataSource) {
    return new SubscriptionRepositoryImpl(planRemoteDataSource, subscriptionRemoteDataSource);
  }
}
