package com.momokotle.agent.data.local;

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
public final class DatabaseInitializer_Factory implements Factory<DatabaseInitializer> {
  @Override
  public DatabaseInitializer get() {
    return newInstance();
  }

  public static DatabaseInitializer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DatabaseInitializer newInstance() {
    return new DatabaseInitializer();
  }

  private static final class InstanceHolder {
    private static final DatabaseInitializer_Factory INSTANCE = new DatabaseInitializer_Factory();
  }
}
