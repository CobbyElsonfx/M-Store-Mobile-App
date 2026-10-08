package com.momokotle.agent.repository;

import com.momokotle.agent.data.remote.LaravelReferralRemoteDataSource;
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
public final class ReferralRepositoryImpl_Factory implements Factory<ReferralRepositoryImpl> {
  private final Provider<LaravelReferralRemoteDataSource> referralRemoteDataSourceProvider;

  public ReferralRepositoryImpl_Factory(
      Provider<LaravelReferralRemoteDataSource> referralRemoteDataSourceProvider) {
    this.referralRemoteDataSourceProvider = referralRemoteDataSourceProvider;
  }

  @Override
  public ReferralRepositoryImpl get() {
    return newInstance(referralRemoteDataSourceProvider.get());
  }

  public static ReferralRepositoryImpl_Factory create(
      Provider<LaravelReferralRemoteDataSource> referralRemoteDataSourceProvider) {
    return new ReferralRepositoryImpl_Factory(referralRemoteDataSourceProvider);
  }

  public static ReferralRepositoryImpl newInstance(
      LaravelReferralRemoteDataSource referralRemoteDataSource) {
    return new ReferralRepositoryImpl(referralRemoteDataSource);
  }
}
