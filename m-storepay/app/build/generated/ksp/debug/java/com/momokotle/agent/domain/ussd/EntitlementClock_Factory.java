package com.momokotle.agent.domain.ussd;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class EntitlementClock_Factory implements Factory<EntitlementClock> {
  @Override
  public EntitlementClock get() {
    return newInstance();
  }

  public static EntitlementClock_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EntitlementClock newInstance() {
    return new EntitlementClock();
  }

  private static final class InstanceHolder {
    private static final EntitlementClock_Factory INSTANCE = new EntitlementClock_Factory();
  }
}
