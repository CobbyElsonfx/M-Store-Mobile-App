package com.momokotle.agent.di;

import com.momokotle.agent.data.remote.MStorepayApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideMStorepayApiFactory implements Factory<MStorepayApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideMStorepayApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MStorepayApiService get() {
    return provideMStorepayApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideMStorepayApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideMStorepayApiFactory(retrofitProvider);
  }

  public static MStorepayApiService provideMStorepayApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideMStorepayApi(retrofit));
  }
}
