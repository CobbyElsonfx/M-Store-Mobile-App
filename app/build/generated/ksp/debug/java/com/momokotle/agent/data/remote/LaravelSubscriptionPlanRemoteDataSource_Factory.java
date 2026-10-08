package com.momokotle.agent.data.remote;

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
public final class LaravelSubscriptionPlanRemoteDataSource_Factory implements Factory<LaravelSubscriptionPlanRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelSubscriptionPlanRemoteDataSource_Factory(
      Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelSubscriptionPlanRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelSubscriptionPlanRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelSubscriptionPlanRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelSubscriptionPlanRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelSubscriptionPlanRemoteDataSource(api);
  }
}
