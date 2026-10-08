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
public final class MarkNotificationReadUseCase_Factory implements Factory<MarkNotificationReadUseCase> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  public MarkNotificationReadUseCase_Factory(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
  }

  @Override
  public MarkNotificationReadUseCase get() {
    return newInstance(notificationRepositoryProvider.get());
  }

  public static MarkNotificationReadUseCase_Factory create(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    return new MarkNotificationReadUseCase_Factory(notificationRepositoryProvider);
  }

  public static MarkNotificationReadUseCase newInstance(
      NotificationRepository notificationRepository) {
    return new MarkNotificationReadUseCase(notificationRepository);
  }
}
