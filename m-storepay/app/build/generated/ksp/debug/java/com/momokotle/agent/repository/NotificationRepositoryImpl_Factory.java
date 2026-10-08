package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelNotificationRemoteDataSource;
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
public final class NotificationRepositoryImpl_Factory implements Factory<NotificationRepositoryImpl> {
  private final Provider<LaravelNotificationRemoteDataSource> notificationRemoteDataSourceProvider;

  public NotificationRepositoryImpl_Factory(
      Provider<LaravelNotificationRemoteDataSource> notificationRemoteDataSourceProvider) {
    this.notificationRemoteDataSourceProvider = notificationRemoteDataSourceProvider;
  }

  @Override
  public NotificationRepositoryImpl get() {
    return newInstance(notificationRemoteDataSourceProvider.get());
  }

  public static NotificationRepositoryImpl_Factory create(
      Provider<LaravelNotificationRemoteDataSource> notificationRemoteDataSourceProvider) {
    return new NotificationRepositoryImpl_Factory(notificationRemoteDataSourceProvider);
  }

  public static NotificationRepositoryImpl newInstance(
      LaravelNotificationRemoteDataSource notificationRemoteDataSource) {
    return new NotificationRepositoryImpl(notificationRemoteDataSource);
  }
}
