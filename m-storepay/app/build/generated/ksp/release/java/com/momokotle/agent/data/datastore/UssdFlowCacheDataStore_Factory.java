package com.momokotle.agent.data.datastore;

import android.content.Context;
import com.google.gson.Gson;
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
public final class UssdFlowCacheDataStore_Factory implements Factory<UssdFlowCacheDataStore> {
  private final Provider<Context> contextProvider;

  private final Provider<Gson> gsonProvider;

  public UssdFlowCacheDataStore_Factory(Provider<Context> contextProvider,
      Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public UssdFlowCacheDataStore get() {
    return newInstance(contextProvider.get(), gsonProvider.get());
  }

  public static UssdFlowCacheDataStore_Factory create(Provider<Context> contextProvider,
      Provider<Gson> gsonProvider) {
    return new UssdFlowCacheDataStore_Factory(contextProvider, gsonProvider);
  }

  public static UssdFlowCacheDataStore newInstance(Context context, Gson gson) {
    return new UssdFlowCacheDataStore(context, gson);
  }
}
