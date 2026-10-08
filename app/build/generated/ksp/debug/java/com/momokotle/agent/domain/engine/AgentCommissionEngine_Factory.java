package com.momokotle.agent.domain.engine;

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
public final class AgentCommissionEngine_Factory implements Factory<AgentCommissionEngine> {
  @Override
  public AgentCommissionEngine get() {
    return newInstance();
  }

  public static AgentCommissionEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AgentCommissionEngine newInstance() {
    return new AgentCommissionEngine();
  }

  private static final class InstanceHolder {
    private static final AgentCommissionEngine_Factory INSTANCE = new AgentCommissionEngine_Factory();
  }
}
