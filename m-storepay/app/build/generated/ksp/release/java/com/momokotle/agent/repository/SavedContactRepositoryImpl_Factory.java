package com.momokotle.agent.repository;

import com.momokotle.agent.database.dao.SavedContactDao;
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
public final class SavedContactRepositoryImpl_Factory implements Factory<SavedContactRepositoryImpl> {
  private final Provider<SavedContactDao> daoProvider;

  public SavedContactRepositoryImpl_Factory(Provider<SavedContactDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public SavedContactRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static SavedContactRepositoryImpl_Factory create(Provider<SavedContactDao> daoProvider) {
    return new SavedContactRepositoryImpl_Factory(daoProvider);
  }

  public static SavedContactRepositoryImpl newInstance(SavedContactDao dao) {
    return new SavedContactRepositoryImpl(dao);
  }
}
