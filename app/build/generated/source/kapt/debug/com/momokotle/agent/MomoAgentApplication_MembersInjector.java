package com.momokotle.agent;

import com.momokotle.agent.data.local.DatabaseInitializer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MomoAgentApplication_MembersInjector implements MembersInjector<MomoAgentApplication> {
  private final Provider<DatabaseInitializer> databaseInitializerProvider;

  public MomoAgentApplication_MembersInjector(
      Provider<DatabaseInitializer> databaseInitializerProvider) {
    this.databaseInitializerProvider = databaseInitializerProvider;
  }

  public static MembersInjector<MomoAgentApplication> create(
      Provider<DatabaseInitializer> databaseInitializerProvider) {
    return new MomoAgentApplication_MembersInjector(databaseInitializerProvider);
  }

  @Override
  public void injectMembers(MomoAgentApplication instance) {
    injectDatabaseInitializer(instance, databaseInitializerProvider.get());
  }

  @InjectedFieldSignature("com.momokotle.agent.MomoAgentApplication.databaseInitializer")
  public static void injectDatabaseInitializer(MomoAgentApplication instance,
      DatabaseInitializer databaseInitializer) {
    instance.databaseInitializer = databaseInitializer;
  }
}
