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
public final class LaravelBusinessSettingsRemoteDataSource_Factory implements Factory<LaravelBusinessSettingsRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelBusinessSettingsRemoteDataSource_Factory(
      Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelBusinessSettingsRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelBusinessSettingsRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelBusinessSettingsRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelBusinessSettingsRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelBusinessSettingsRemoteDataSource(api);
  }
}
