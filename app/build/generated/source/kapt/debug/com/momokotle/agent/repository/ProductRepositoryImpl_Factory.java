package com.momokotle.agent.repository;

import com.momokotle.agent.data.firestore.FirestoreProductDataSource;
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

  private final Provider<FirestoreProductDataSource> firestoreProductDataSourceProvider;

  private final Provider<CoroutineScope> applicationScopeProvider;

  public ProductRepositoryImpl_Factory(Provider<StoreProductDao> storeProductDaoProvider,
      Provider<FirestoreProductDataSource> firestoreProductDataSourceProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    this.storeProductDaoProvider = storeProductDaoProvider;
    this.firestoreProductDataSourceProvider = firestoreProductDataSourceProvider;
    this.applicationScopeProvider = applicationScopeProvider;
  }

  @Override
  public ProductRepositoryImpl get() {
    return newInstance(storeProductDaoProvider.get(), firestoreProductDataSourceProvider.get(), applicationScopeProvider.get());
  }

  public static ProductRepositoryImpl_Factory create(
      Provider<StoreProductDao> storeProductDaoProvider,
      Provider<FirestoreProductDataSource> firestoreProductDataSourceProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    return new ProductRepositoryImpl_Factory(storeProductDaoProvider, firestoreProductDataSourceProvider, applicationScopeProvider);
  }

  public static ProductRepositoryImpl newInstance(StoreProductDao storeProductDao,
      FirestoreProductDataSource firestoreProductDataSource, CoroutineScope applicationScope) {
    return new ProductRepositoryImpl(storeProductDao, firestoreProductDataSource, applicationScope);
  }
}
