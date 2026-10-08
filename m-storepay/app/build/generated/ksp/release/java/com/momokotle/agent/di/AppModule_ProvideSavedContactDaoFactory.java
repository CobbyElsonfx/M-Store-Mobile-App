package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.SavedContactDao;
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
public final class AppModule_ProvideSavedContactDaoFactory implements Factory<SavedContactDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideSavedContactDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SavedContactDao get() {
    return provideSavedContactDao(dbProvider.get());
  }

  public static AppModule_ProvideSavedContactDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideSavedContactDaoFactory(dbProvider);
  }

  public static SavedContactDao provideSavedContactDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSavedContactDao(db));
  }
}
