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
public final class LaravelSubscriptionRemoteDataSource_Factory implements Factory<LaravelSubscriptionRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelSubscriptionRemoteDataSource_Factory(Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelSubscriptionRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelSubscriptionRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelSubscriptionRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelSubscriptionRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelSubscriptionRemoteDataSource(api);
  }
}
