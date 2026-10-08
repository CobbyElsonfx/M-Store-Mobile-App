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
public final class ObserveNotificationsUseCase_Factory implements Factory<ObserveNotificationsUseCase> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  public ObserveNotificationsUseCase_Factory(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
  }

  @Override
  public ObserveNotificationsUseCase get() {
    return newInstance(notificationRepositoryProvider.get());
  }

  public static ObserveNotificationsUseCase_Factory create(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    return new ObserveNotificationsUseCase_Factory(notificationRepositoryProvider);
  }

  public static ObserveNotificationsUseCase newInstance(
      NotificationRepository notificationRepository) {
    return new ObserveNotificationsUseCase(notificationRepository);
  }
}
