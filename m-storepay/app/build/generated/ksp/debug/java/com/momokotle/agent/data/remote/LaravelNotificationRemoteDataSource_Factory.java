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
public final class LaravelNotificationRemoteDataSource_Factory implements Factory<LaravelNotificationRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelNotificationRemoteDataSource_Factory(Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelNotificationRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelNotificationRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelNotificationRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelNotificationRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelNotificationRemoteDataSource(api);
  }
}
