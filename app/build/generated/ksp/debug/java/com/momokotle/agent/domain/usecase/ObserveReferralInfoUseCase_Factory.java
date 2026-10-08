package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.domain.repository.ReferralRepository;
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
public final class ObserveReferralInfoUseCase_Factory implements Factory<ObserveReferralInfoUseCase> {
  private final Provider<ReferralRepository> referralRepositoryProvider;

  public ObserveReferralInfoUseCase_Factory(
      Provider<ReferralRepository> referralRepositoryProvider) {
    this.referralRepositoryProvider = referralRepositoryProvider;
  }

  @Override
  public ObserveReferralInfoUseCase get() {
    return newInstance(referralRepositoryProvider.get());
  }

  public static ObserveReferralInfoUseCase_Factory create(
      Provider<ReferralRepository> referralRepositoryProvider) {
    return new ObserveReferralInfoUseCase_Factory(referralRepositoryProvider);
  }

  public static ObserveReferralInfoUseCase newInstance(ReferralRepository referralRepository) {
    return new ObserveReferralInfoUseCase(referralRepository);
  }
}
