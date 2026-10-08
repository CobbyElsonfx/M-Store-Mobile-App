package com.momokotle.agent.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AccessibilityRepositoryImpl_Factory implements Factory<AccessibilityRepositoryImpl> {
  private final Provider<Context> contextProvider;

  public AccessibilityRepositoryImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AccessibilityRepositoryImpl get() {
    return newInstance(contextProvider.get());
  }

  public static AccessibilityRepositoryImpl_Factory create(Provider<Context> contextProvider) {
    return new AccessibilityRepositoryImpl_Factory(contextProvider);
  }

  public static AccessibilityRepositoryImpl newInstance(Context context) {
    return new AccessibilityRepositoryImpl(context);
  }
}
