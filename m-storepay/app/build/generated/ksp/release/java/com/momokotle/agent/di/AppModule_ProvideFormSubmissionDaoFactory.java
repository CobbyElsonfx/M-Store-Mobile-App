package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.FormSubmissionDao;
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
public final class AppModule_ProvideFormSubmissionDaoFactory implements Factory<FormSubmissionDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideFormSubmissionDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FormSubmissionDao get() {
    return provideFormSubmissionDao(dbProvider.get());
  }

  public static AppModule_ProvideFormSubmissionDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideFormSubmissionDaoFactory(dbProvider);
  }

  public static FormSubmissionDao provideFormSubmissionDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFormSubmissionDao(db));
  }
}
