package com.momokotle.agent.presentation.notifications;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.usecase.MarkAllNotificationsReadUseCase;
import com.momokotle.agent.domain.usecase.MarkNotificationReadUseCase;
import com.momokotle.agent.domain.usecase.ObserveNotificationsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NotificationsViewModel_Factory implements Factory<NotificationsViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ObserveNotificationsUseCase> observeNotificationsUseCaseProvider;

  private final Provider<MarkNotificationReadUseCase> markNotificationReadUseCaseProvider;

  private final Provider<MarkAllNotificationsReadUseCase> markAllNotificationsReadUseCaseProvider;

  public NotificationsViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveNotificationsUseCase> observeNotificationsUseCaseProvider,
      Provider<MarkNotificationReadUseCase> markNotificationReadUseCaseProvider,
      Provider<MarkAllNotificationsReadUseCase> markAllNotificationsReadUseCaseProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.observeNotificationsUseCaseProvider = observeNotificationsUseCaseProvider;
    this.markNotificationReadUseCaseProvider = markNotificationReadUseCaseProvider;
    this.markAllNotificationsReadUseCaseProvider = markAllNotificationsReadUseCaseProvider;
  }

  @Override
  public NotificationsViewModel get() {
    return newInstance(authRepositoryProvider.get(), observeNotificationsUseCaseProvider.get(), markNotificationReadUseCaseProvider.get(), markAllNotificationsReadUseCaseProvider.get());
  }

  public static NotificationsViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ObserveNotificationsUseCase> observeNotificationsUseCaseProvider,
      Provider<MarkNotificationReadUseCase> markNotificationReadUseCaseProvider,
      Provider<MarkAllNotificationsReadUseCase> markAllNotificationsReadUseCaseProvider) {
    return new NotificationsViewModel_Factory(authRepositoryProvider, observeNotificationsUseCaseProvider, markNotificationReadUseCaseProvider, markAllNotificationsReadUseCaseProvider);
  }

  public static NotificationsViewModel newInstance(AuthRepository authRepository,
      ObserveNotificationsUseCase observeNotificationsUseCase,
      MarkNotificationReadUseCase markNotificationReadUseCase,
      MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase) {
    return new NotificationsViewModel(authRepository, observeNotificationsUseCase, markNotificationReadUseCase, markAllNotificationsReadUseCase);
  }
}
