package com.momokotle.agent.presentation.submissions;

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
public final class SubmissionDetailViewModel_Factory implements Factory<SubmissionDetailViewModel> {
  private final Provider<FormSubmissionRepository> formSubmissionRepositoryProvider;

  public SubmissionDetailViewModel_Factory(
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    this.formSubmissionRepositoryProvider = formSubmissionRepositoryProvider;
  }

  @Override
  public SubmissionDetailViewModel get() {
    return newInstance(formSubmissionRepositoryProvider.get());
  }

  public static SubmissionDetailViewModel_Factory create(
      Provider<FormSubmissionRepository> formSubmissionRepositoryProvider) {
    return new SubmissionDetailViewModel_Factory(formSubmissionRepositoryProvider);
  }

  public static SubmissionDetailViewModel newInstance(
      FormSubmissionRepository formSubmissionRepository) {
    return new SubmissionDetailViewModel(formSubmissionRepository);
  }
}
