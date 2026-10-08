package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelProductRemoteDataSource;
import com.momokotle.agent.database.dao.StoreProductDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.momokotle.agent.di.ApplicationScope")
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
public final class ProductRepositoryImpl_Factory implements Factory<ProductRepositoryImpl> {
  private final Provider<StoreProductDao> storeProductDaoProvider;

  private final Provider<LaravelProductRemoteDataSource> laravelProductRemoteDataSourceProvider;

  private final Provider<CoroutineScope> applicationScopeProvider;

  public ProductRepositoryImpl_Factory(Provider<StoreProductDao> storeProductDaoProvider,
      Provider<LaravelProductRemoteDataSource> laravelProductRemoteDataSourceProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    this.storeProductDaoProvider = storeProductDaoProvider;
    this.laravelProductRemoteDataSourceProvider = laravelProductRemoteDataSourceProvider;
    this.applicationScopeProvider = applicationScopeProvider;
  }

  @Override
  public ProductRepositoryImpl get() {
    return newInstance(storeProductDaoProvider.get(), laravelProductRemoteDataSourceProvider.get(), applicationScopeProvider.get());
  }

  public static ProductRepositoryImpl_Factory create(
      Provider<StoreProductDao> storeProductDaoProvider,
      Provider<LaravelProductRemoteDataSource> laravelProductRemoteDataSourceProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    return new ProductRepositoryImpl_Factory(storeProductDaoProvider, laravelProductRemoteDataSourceProvider, applicationScopeProvider);
  }

  public static ProductRepositoryImpl newInstance(StoreProductDao storeProductDao,
      LaravelProductRemoteDataSource laravelProductRemoteDataSource,
      CoroutineScope applicationScope) {
    return new ProductRepositoryImpl(storeProductDao, laravelProductRemoteDataSource, applicationScope);
  }
}
