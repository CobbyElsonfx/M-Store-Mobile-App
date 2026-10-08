package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelBusinessSettingsRemoteDataSource;
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
public final class BusinessSettingsRepositoryImpl_Factory implements Factory<BusinessSettingsRepositoryImpl> {
  private final Provider<LaravelBusinessSettingsRemoteDataSource> remoteDataSourceProvider;

  public BusinessSettingsRepositoryImpl_Factory(
      Provider<LaravelBusinessSettingsRemoteDataSource> remoteDataSourceProvider) {
    this.remoteDataSourceProvider = remoteDataSourceProvider;
  }

  @Override
  public BusinessSettingsRepositoryImpl get() {
    return newInstance(remoteDataSourceProvider.get());
  }

  public static BusinessSettingsRepositoryImpl_Factory create(
      Provider<LaravelBusinessSettingsRemoteDataSource> remoteDataSourceProvider) {
    return new BusinessSettingsRepositoryImpl_Factory(remoteDataSourceProvider);
  }

  public static BusinessSettingsRepositoryImpl newInstance(
      LaravelBusinessSettingsRemoteDataSource remoteDataSource) {
    return new BusinessSettingsRepositoryImpl(remoteDataSource);
  }
}
