package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.MStorepayApiService;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.utils.NetworkMonitor;
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
public final class FormSubmissionRepositoryImpl_Factory implements Factory<FormSubmissionRepositoryImpl> {
  private final Provider<FormSubmissionDao> formSubmissionDaoProvider;

  private final Provider<MStorepayApiService> apiProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public FormSubmissionRepositoryImpl_Factory(Provider<FormSubmissionDao> formSubmissionDaoProvider,
      Provider<MStorepayApiService> apiProvider, Provider<NetworkMonitor> networkMonitorProvider) {
    this.formSubmissionDaoProvider = formSubmissionDaoProvider;
    this.apiProvider = apiProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public FormSubmissionRepositoryImpl get() {
    return newInstance(formSubmissionDaoProvider.get(), apiProvider.get(), networkMonitorProvider.get());
  }

  public static FormSubmissionRepositoryImpl_Factory create(
      Provider<FormSubmissionDao> formSubmissionDaoProvider,
      Provider<MStorepayApiService> apiProvider, Provider<NetworkMonitor> networkMonitorProvider) {
    return new FormSubmissionRepositoryImpl_Factory(formSubmissionDaoProvider, apiProvider, networkMonitorProvider);
  }

  public static FormSubmissionRepositoryImpl newInstance(FormSubmissionDao formSubmissionDao,
      MStorepayApiService api, NetworkMonitor networkMonitor) {
    return new FormSubmissionRepositoryImpl(formSubmissionDao, api, networkMonitor);
  }
}
