package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.StoreOrderDao;
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
public final class AppModule_ProvideStoreOrderDaoFactory implements Factory<StoreOrderDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideStoreOrderDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StoreOrderDao get() {
    return provideStoreOrderDao(dbProvider.get());
  }

  public static AppModule_ProvideStoreOrderDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideStoreOrderDaoFactory(dbProvider);
  }

  public static StoreOrderDao provideStoreOrderDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStoreOrderDao(db));
  }
}
