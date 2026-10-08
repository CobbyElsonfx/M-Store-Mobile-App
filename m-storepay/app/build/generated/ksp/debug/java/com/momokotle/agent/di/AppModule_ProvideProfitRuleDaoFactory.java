package com.momokotle.agent.di;

import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.ProfitRuleDao;
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
public final class AppModule_ProvideProfitRuleDaoFactory implements Factory<ProfitRuleDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideProfitRuleDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProfitRuleDao get() {
    return provideProfitRuleDao(dbProvider.get());
  }

  public static AppModule_ProvideProfitRuleDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideProfitRuleDaoFactory(dbProvider);
  }

  public static ProfitRuleDao provideProfitRuleDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProfitRuleDao(db));
  }
}
