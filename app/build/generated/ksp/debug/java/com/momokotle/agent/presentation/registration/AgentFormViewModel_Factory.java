package com.momokotle.agent.presentation.registration;

import androidx.lifecycle.SavedStateHandle;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase;
import com.momokotle.agent.utils.FileDocumentHelper;
import com.momokotle.agent.utils.NetworkMonitor;
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
public final class AgentFormViewModel_Factory implements Factory<AgentFormViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<FormSubmissionRepository> formSubmissionRepositoryProvider;

  private final Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<FileDocumentHelper> fileDocumentHelperProvider;

  public AgentFormViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider,
      Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<FileDocumentHelper> fileDocumentHelperProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.formSubmissionRepositoryProvider = formSubmissionRepositoryProvider;
    this.getAuthenticatedUserUseCaseProvider = getAuthenticatedUserUseCaseProvider;
    this.networkMonitorProvider = networkMonitorProvider;
    this.fileDocumentHelperProvider = fileDocumentHelperProvider;
  }

  @Override
  public AgentFormViewModel get() {
    return newInstance(savedStateHandleProvider.get(), formSubmissionRepositoryProvider.get(), getAuthenticatedUserUseCaseProvider.get(), networkMonitorProvider.get(), fileDocumentHelperProvider.get());
  }

  public static AgentFormViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider,
      Provider<GetAuthenticatedUserUseCase> getAuthenticatedUserUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<FileDocumentHelper> fileDocumentHelperProvider) {
    return new AgentFormViewModel_Factory(savedStateHandleProvider, formSubmissionRepositoryProvider, getAuthenticatedUserUseCaseProvider, networkMonitorProvider, fileDocumentHelperProvider);
  }

  public static AgentFormViewModel newInstance(SavedStateHandle savedStateHandle,
      FormSubmissionRepository formSubmissionRepository,
      GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, NetworkMonitor networkMonitor,
      FileDocumentHelper fileDocumentHelper) {
    return new AgentFormViewModel(savedStateHandle, formSubmissionRepository, getAuthenticatedUserUseCase, networkMonitor, fileDocumentHelper);
  }
}
