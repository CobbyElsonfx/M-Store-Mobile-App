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
public final class LaravelApplicationRemoteDataSource_Factory implements Factory<LaravelApplicationRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelApplicationRemoteDataSource_Factory(Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelApplicationRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelApplicationRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelApplicationRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelApplicationRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelApplicationRemoteDataSource(api);
  }
}
