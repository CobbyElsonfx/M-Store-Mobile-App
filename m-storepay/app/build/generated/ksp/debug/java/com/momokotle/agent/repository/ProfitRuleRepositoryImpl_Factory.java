package com.momokotle.agent.repository;

import com.momokotle.agent.database.dao.ProfitRuleDao;
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
public final class ProfitRuleRepositoryImpl_Factory implements Factory<ProfitRuleRepositoryImpl> {
  private final Provider<ProfitRuleDao> daoProvider;

  public ProfitRuleRepositoryImpl_Factory(Provider<ProfitRuleDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ProfitRuleRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ProfitRuleRepositoryImpl_Factory create(Provider<ProfitRuleDao> daoProvider) {
    return new ProfitRuleRepositoryImpl_Factory(daoProvider);
  }

  public static ProfitRuleRepositoryImpl newInstance(ProfitRuleDao dao) {
    return new ProfitRuleRepositoryImpl(dao);
  }
}
