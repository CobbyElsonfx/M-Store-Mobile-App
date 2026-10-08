package com.momokotle.agent.presentation.referral;

import android.content.Context;
import com.momokotle.agent.data.datastore.ReferralDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ReferralViewModel_Factory implements Factory<ReferralViewModel> {
  private final Provider<ReferralDataStore> referralDataStoreProvider;

  private final Provider<Context> contextProvider;

  public ReferralViewModel_Factory(Provider<ReferralDataStore> referralDataStoreProvider,
      Provider<Context> contextProvider) {
    this.referralDataStoreProvider = referralDataStoreProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ReferralViewModel get() {
    return newInstance(referralDataStoreProvider.get(), contextProvider.get());
  }

  public static ReferralViewModel_Factory create(
      Provider<ReferralDataStore> referralDataStoreProvider, Provider<Context> contextProvider) {
    return new ReferralViewModel_Factory(referralDataStoreProvider, contextProvider);
  }

  public static ReferralViewModel newInstance(ReferralDataStore referralDataStore,
      Context context) {
    return new ReferralViewModel(referralDataStore, context);
  }
}
