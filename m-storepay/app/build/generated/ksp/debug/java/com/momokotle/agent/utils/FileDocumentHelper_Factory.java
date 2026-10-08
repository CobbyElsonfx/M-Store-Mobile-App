package com.momokotle.agent.utils;

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
public final class FileDocumentHelper_Factory implements Factory<FileDocumentHelper> {
  private final Provider<Context> contextProvider;

  public FileDocumentHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FileDocumentHelper get() {
    return newInstance(contextProvider.get());
  }

  public static FileDocumentHelper_Factory create(Provider<Context> contextProvider) {
    return new FileDocumentHelper_Factory(contextProvider);
  }

  public static FileDocumentHelper newInstance(Context context) {
    return new FileDocumentHelper(context);
  }
}
