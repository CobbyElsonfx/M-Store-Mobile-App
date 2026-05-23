package com.momokotle.agent.di;

import android.content.Context;
import com.momokotle.agent.ussd.UssdDialHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUssdDialHelperFactory implements Factory<UssdDialHelper> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideUssdDialHelperFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public UssdDialHelper get() {
    return provideUssdDialHelper(contextProvider.get());
  }

  public static AppModule_ProvideUssdDialHelperFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideUssdDialHelperFactory(contextProvider);
  }

  public static UssdDialHelper provideUssdDialHelper(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUssdDialHelper(context));
  }
}
