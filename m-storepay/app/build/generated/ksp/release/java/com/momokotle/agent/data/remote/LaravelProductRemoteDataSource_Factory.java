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
public final class LaravelProductRemoteDataSource_Factory implements Factory<LaravelProductRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelProductRemoteDataSource_Factory(Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelProductRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelProductRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelProductRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelProductRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelProductRemoteDataSource(api);
  }
}
