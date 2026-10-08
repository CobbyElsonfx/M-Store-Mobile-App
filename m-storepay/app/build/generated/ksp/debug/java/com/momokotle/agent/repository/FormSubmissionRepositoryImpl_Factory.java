package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelApplicationRemoteDataSource;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.momokotle.agent.di.ApplicationScope")
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
public final class FormSubmissionRepositoryImpl_Factory implements Factory<FormSubmissionRepositoryImpl> {
  private final Provider<FormSubmissionDao> formSubmissionDaoProvider;

  private final Provider<LaravelApplicationRemoteDataSource> laravelApplicationRemoteDataSourceProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<CoroutineScope> applicationScopeProvider;

  public FormSubmissionRepositoryImpl_Factory(Provider<FormSubmissionDao> formSubmissionDaoProvider,
      Provider<LaravelApplicationRemoteDataSource> laravelApplicationRemoteDataSourceProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    this.formSubmissionDaoProvider = formSubmissionDaoProvider;
    this.laravelApplicationRemoteDataSourceProvider = laravelApplicationRemoteDataSourceProvider;
    this.networkMonitorProvider = networkMonitorProvider;
    this.applicationScopeProvider = applicationScopeProvider;
  }

  @Override
  public FormSubmissionRepositoryImpl get() {
    return newInstance(formSubmissionDaoProvider.get(), laravelApplicationRemoteDataSourceProvider.get(), networkMonitorProvider.get(), applicationScopeProvider.get());
  }

  public static FormSubmissionRepositoryImpl_Factory create(
      Provider<FormSubmissionDao> formSubmissionDaoProvider,
      Provider<LaravelApplicationRemoteDataSource> laravelApplicationRemoteDataSourceProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    return new FormSubmissionRepositoryImpl_Factory(formSubmissionDaoProvider, laravelApplicationRemoteDataSourceProvider, networkMonitorProvider, applicationScopeProvider);
  }

  public static FormSubmissionRepositoryImpl newInstance(FormSubmissionDao formSubmissionDao,
      LaravelApplicationRemoteDataSource laravelApplicationRemoteDataSource,
      NetworkMonitor networkMonitor, CoroutineScope applicationScope) {
    return new FormSubmissionRepositoryImpl(formSubmissionDao, laravelApplicationRemoteDataSource, networkMonitor, applicationScope);
  }
}
