package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.SystemUssdFlowDao;
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
public final class AppModule_ProvideSystemUssdFlowDaoFactory implements Factory<SystemUssdFlowDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideSystemUssdFlowDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SystemUssdFlowDao get() {
    return provideSystemUssdFlowDao(dbProvider.get());
  }

  public static AppModule_ProvideSystemUssdFlowDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideSystemUssdFlowDaoFactory(dbProvider);
  }

  public static SystemUssdFlowDao provideSystemUssdFlowDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSystemUssdFlowDao(db));
  }
}
