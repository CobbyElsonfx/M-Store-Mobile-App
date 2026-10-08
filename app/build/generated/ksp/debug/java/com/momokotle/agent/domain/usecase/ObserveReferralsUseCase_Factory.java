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
public final class ObserveReferralsUseCase_Factory implements Factory<ObserveReferralsUseCase> {
  private final Provider<ReferralRepository> referralRepositoryProvider;

  public ObserveReferralsUseCase_Factory(Provider<ReferralRepository> referralRepositoryProvider) {
    this.referralRepositoryProvider = referralRepositoryProvider;
  }

  @Override
  public ObserveReferralsUseCase get() {
    return newInstance(referralRepositoryProvider.get());
  }

  public static ObserveReferralsUseCase_Factory create(
      Provider<ReferralRepository> referralRepositoryProvider) {
    return new ObserveReferralsUseCase_Factory(referralRepositoryProvider);
  }

  public static ObserveReferralsUseCase newInstance(ReferralRepository referralRepository) {
    return new ObserveReferralsUseCase(referralRepository);
  }
}
