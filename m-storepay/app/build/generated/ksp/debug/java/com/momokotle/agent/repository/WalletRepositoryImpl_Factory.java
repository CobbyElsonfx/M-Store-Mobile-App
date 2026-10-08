package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelWalletRemoteDataSource;
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
public final class WalletRepositoryImpl_Factory implements Factory<WalletRepositoryImpl> {
  private final Provider<LaravelWalletRemoteDataSource> walletRemoteDataSourceProvider;

  public WalletRepositoryImpl_Factory(
      Provider<LaravelWalletRemoteDataSource> walletRemoteDataSourceProvider) {
    this.walletRemoteDataSourceProvider = walletRemoteDataSourceProvider;
  }

  @Override
  public WalletRepositoryImpl get() {
    return newInstance(walletRemoteDataSourceProvider.get());
  }

  public static WalletRepositoryImpl_Factory create(
      Provider<LaravelWalletRemoteDataSource> walletRemoteDataSourceProvider) {
    return new WalletRepositoryImpl_Factory(walletRemoteDataSourceProvider);
  }

  public static WalletRepositoryImpl newInstance(
      LaravelWalletRemoteDataSource walletRemoteDataSource) {
    return new WalletRepositoryImpl(walletRemoteDataSource);
  }
}
