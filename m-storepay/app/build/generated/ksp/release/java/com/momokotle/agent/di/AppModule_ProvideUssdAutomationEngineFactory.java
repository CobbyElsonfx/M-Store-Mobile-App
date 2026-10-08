package com.momokotle.agent.di;

import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.utils.AutomationLogger;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUssdAutomationEngineFactory implements Factory<UssdAutomationEngine> {
  private final Provider<AutomationLogger> loggerProvider;

  public AppModule_ProvideUssdAutomationEngineFactory(Provider<AutomationLogger> loggerProvider) {
    this.loggerProvider = loggerProvider;
  }

  @Override
  public UssdAutomationEngine get() {
    return provideUssdAutomationEngine(loggerProvider.get());
  }

  public static AppModule_ProvideUssdAutomationEngineFactory create(
      Provider<AutomationLogger> loggerProvider) {
    return new AppModule_ProvideUssdAutomationEngineFactory(loggerProvider);
  }

  public static UssdAutomationEngine provideUssdAutomationEngine(AutomationLogger logger) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUssdAutomationEngine(logger));
  }
}
