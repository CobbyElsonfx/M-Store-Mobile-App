package com.momokotle.agent.accessibility;

import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.utils.AutomationLogger;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MomoAccessibilityService_MembersInjector implements MembersInjector<MomoAccessibilityService> {
  private final Provider<UssdAutomationEngine> engineProvider;

  private final Provider<AutomationLogger> loggerProvider;

  private final Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider;

  private final Provider<AccessibilityRepository> accessibilityRepositoryProvider;

  public MomoAccessibilityService_MembersInjector(Provider<UssdAutomationEngine> engineProvider,
      Provider<AutomationLogger> loggerProvider,
      Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider) {
    this.engineProvider = engineProvider;
    this.loggerProvider = loggerProvider;
    this.updateTransactionStatusUseCaseProvider = updateTransactionStatusUseCaseProvider;
    this.accessibilityRepositoryProvider = accessibilityRepositoryProvider;
  }

  public static MembersInjector<MomoAccessibilityService> create(
      Provider<UssdAutomationEngine> engineProvider, Provider<AutomationLogger> loggerProvider,
      Provider<UpdateTransactionStatusUseCase> updateTransactionStatusUseCaseProvider,
      Provider<AccessibilityRepository> accessibilityRepositoryProvider) {
    return new MomoAccessibilityService_MembersInjector(engineProvider, loggerProvider, updateTransactionStatusUseCaseProvider, accessibilityRepositoryProvider);
  }

  @Override
  public void injectMembers(MomoAccessibilityService instance) {
    injectEngine(instance, engineProvider.get());
    injectLogger(instance, loggerProvider.get());
    injectUpdateTransactionStatusUseCase(instance, updateTransactionStatusUseCaseProvider.get());
    injectAccessibilityRepository(instance, accessibilityRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.momokotle.agent.accessibility.MomoAccessibilityService.engine")
  public static void injectEngine(MomoAccessibilityService instance, UssdAutomationEngine engine) {
    instance.engine = engine;
  }

  @InjectedFieldSignature("com.momokotle.agent.accessibility.MomoAccessibilityService.logger")
  public static void injectLogger(MomoAccessibilityService instance, AutomationLogger logger) {
    instance.logger = logger;
  }

  @InjectedFieldSignature("com.momokotle.agent.accessibility.MomoAccessibilityService.updateTransactionStatusUseCase")
  public static void injectUpdateTransactionStatusUseCase(MomoAccessibilityService instance,
      UpdateTransactionStatusUseCase updateTransactionStatusUseCase) {
    instance.updateTransactionStatusUseCase = updateTransactionStatusUseCase;
  }

  @InjectedFieldSignature("com.momokotle.agent.accessibility.MomoAccessibilityService.accessibilityRepository")
  public static void injectAccessibilityRepository(MomoAccessibilityService instance,
      AccessibilityRepository accessibilityRepository) {
    instance.accessibilityRepository = accessibilityRepository;
  }
}
