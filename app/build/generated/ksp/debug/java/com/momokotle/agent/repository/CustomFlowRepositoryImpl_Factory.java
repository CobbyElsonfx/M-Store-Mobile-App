package com.momokotle.agent.repository;

import com.momokotle.agent.database.dao.CustomFlowDao;
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
public final class CustomFlowRepositoryImpl_Factory implements Factory<CustomFlowRepositoryImpl> {
  private final Provider<CustomFlowDao> daoProvider;

  public CustomFlowRepositoryImpl_Factory(Provider<CustomFlowDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public CustomFlowRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static CustomFlowRepositoryImpl_Factory create(Provider<CustomFlowDao> daoProvider) {
    return new CustomFlowRepositoryImpl_Factory(daoProvider);
  }

  public static CustomFlowRepositoryImpl newInstance(CustomFlowDao dao) {
    return new CustomFlowRepositoryImpl(dao);
  }
}
