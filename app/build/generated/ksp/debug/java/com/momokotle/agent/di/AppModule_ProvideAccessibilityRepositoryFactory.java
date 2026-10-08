package com.momokotle.agent.di;

import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.repository.AccessibilityRepositoryImpl;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideAccessibilityRepositoryFactory implements Factory<AccessibilityRepository> {
  private final Provider<AccessibilityRepositoryImpl> implProvider;

  public AppModule_ProvideAccessibilityRepositoryFactory(
      Provider<AccessibilityRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public AccessibilityRepository get() {
    return provideAccessibilityRepository(implProvider.get());
  }

  public static AppModule_ProvideAccessibilityRepositoryFactory create(
      Provider<AccessibilityRepositoryImpl> implProvider) {
    return new AppModule_ProvideAccessibilityRepositoryFactory(implProvider);
  }

  public static AccessibilityRepository provideAccessibilityRepository(
      AccessibilityRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAccessibilityRepository(impl));
  }
}
