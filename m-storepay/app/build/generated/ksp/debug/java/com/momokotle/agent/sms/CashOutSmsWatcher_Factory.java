package com.momokotle.agent.sms;

import android.content.Context;
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase;
import com.momokotle.agent.utils.AutomationLogger;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "dagger.hilt.android.qualifiers.ApplicationContext",
    "com.momokotle.agent.di.ApplicationScope"
})
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
public final class CashOutSmsWatcher_Factory implements Factory<CashOutSmsWatcher> {
  private final Provider<Context> contextProvider;

  private final Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider;

  private final Provider<AutomationLogger> loggerProvider;

  private final Provider<CoroutineScope> scopeProvider;

  public CashOutSmsWatcher_Factory(Provider<Context> contextProvider,
      Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider,
      Provider<AutomationLogger> loggerProvider, Provider<CoroutineScope> scopeProvider) {
    this.contextProvider = contextProvider;
    this.updateTransactionStatusUseCaseProvider = updateTransactionStatusUseCaseProvider;
    this.loggerProvider = loggerProvider;
    this.scopeProvider = scopeProvider;
  }

  @Override
  public CashOutSmsWatcher get() {
    return newInstance(contextProvider.get(), updateTransactionStatusUseCaseProvider.get(), loggerProvider.get(), scopeProvider.get());
  }

  public static CashOutSmsWatcher_Factory create(Provider<Context> contextProvider,
      Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider,
      Provider<AutomationLogger> loggerProvider, Provider<CoroutineScope> scopeProvider) {
    return new CashOutSmsWatcher_Factory(contextProvider, updateTransactionStatusUseCaseProvider, loggerProvider, scopeProvider);
  }

  public static CashOutSmsWatcher newInstance(Context context,
      UpdateTransactionStatusUseCase updateTransactionStatusUseCase, AutomationLogger logger,
      CoroutineScope scope) {
    return new CashOutSmsWatcher(context, updateTransactionStatusUseCase, logger, scope);
  }
}
