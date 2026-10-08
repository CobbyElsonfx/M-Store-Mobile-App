package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.StoreProductDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AppModule_ProvideStoreProductDaoFactory implements Factory<StoreProductDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideStoreProductDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StoreProductDao get() {
    return provideStoreProductDao(dbProvider.get());
  }

  public static AppModule_ProvideStoreProductDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideStoreProductDaoFactory(dbProvider);
  }

  public static StoreProductDao provideStoreProductDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStoreProductDao(db));
  }
}
