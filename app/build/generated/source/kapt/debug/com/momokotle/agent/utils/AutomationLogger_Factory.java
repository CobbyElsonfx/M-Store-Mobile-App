package com.momokotle.agent.utils;

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
public final class AutomationLogger_Factory implements Factory<AutomationLogger> {
  @Override
  public AutomationLogger get() {
    return newInstance();
  }

  public static AutomationLogger_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AutomationLogger newInstance() {
    return new AutomationLogger();
  }

  private static final class InstanceHolder {
    private static final AutomationLogger_Factory INSTANCE = new AutomationLogger_Factory();
  }
}
