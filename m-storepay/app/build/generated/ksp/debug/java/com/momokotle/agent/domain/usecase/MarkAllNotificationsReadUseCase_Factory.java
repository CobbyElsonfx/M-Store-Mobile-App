package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.NotificationRepository;
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
public final class MarkAllNotificationsReadUseCase_Factory implements Factory<MarkAllNotificationsReadUseCase> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  public MarkAllNotificationsReadUseCase_Factory(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
  }

  @Override
  public MarkAllNotificationsReadUseCase get() {
    return newInstance(notificationRepositoryProvider.get());
  }

  public static MarkAllNotificationsReadUseCase_Factory create(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    return new MarkAllNotificationsReadUseCase_Factory(notificationRepositoryProvider);
  }

  public static MarkAllNotificationsReadUseCase newInstance(
      NotificationRepository notificationRepository) {
    return new MarkAllNotificationsReadUseCase(notificationRepository);
  }
}
