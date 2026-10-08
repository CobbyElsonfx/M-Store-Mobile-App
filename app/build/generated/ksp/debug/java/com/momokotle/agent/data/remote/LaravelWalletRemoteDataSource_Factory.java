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
public final class LaravelWalletRemoteDataSource_Factory implements Factory<LaravelWalletRemoteDataSource> {
  private final Provider<MStorepayApiService> apiProvider;

  public LaravelWalletRemoteDataSource_Factory(Provider<MStorepayApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LaravelWalletRemoteDataSource get() {
    return newInstance(apiProvider.get());
  }

  public static LaravelWalletRemoteDataSource_Factory create(
      Provider<MStorepayApiService> apiProvider) {
    return new LaravelWalletRemoteDataSource_Factory(apiProvider);
  }

  public static LaravelWalletRemoteDataSource newInstance(MStorepayApiService api) {
    return new LaravelWalletRemoteDataSource(api);
  }
}
