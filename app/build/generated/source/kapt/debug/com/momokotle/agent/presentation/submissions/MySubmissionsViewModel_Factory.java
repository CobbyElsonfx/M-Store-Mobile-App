package com.momokotle.agent.presentation.submissions;

import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
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
public final class MySubmissionsViewModel_Factory implements Factory<MySubmissionsViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FormSubmissionRepository> formSubmissionRepositoryProvider;

  public MySubmissionsViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.formSubmissionRepositoryProvider = formSubmissionRepositoryProvider;
  }

  @Override
  public MySubmissionsViewModel get() {
    return newInstance(authRepositoryProvider.get(), formSubmissionRepositoryProvider.get());
  }

  public static MySubmissionsViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    return new MySubmissionsViewModel_Factory(authRepositoryProvider, formSubmissionRepositoryProvider);
  }

  public static MySubmissionsViewModel newInstance(AuthRepository authRepository,
      FormSubmissionRepository formSubmissionRepository) {
    return new MySubmissionsViewModel(authRepository, formSubmissionRepository);
  }
}
