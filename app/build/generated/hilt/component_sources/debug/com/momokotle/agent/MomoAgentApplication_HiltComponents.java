package com.momokotle.agent;

import com.momokotle.agent.accessibility.MomoAccessibilityService_GeneratedInjector;
import com.momokotle.agent.di.AppModule;
import com.momokotle.agent.di.FirebaseModule;
import com.momokotle.agent.di.NetworkModule;
import com.momokotle.agent.di.RepositoryModule;
import com.momokotle.agent.presentation.MainActivity_GeneratedInjector;
import com.momokotle.agent.presentation.auth.LoginViewModel_HiltModules;
import com.momokotle.agent.presentation.auth.RegisterViewModel_HiltModules;
import com.momokotle.agent.presentation.auth.SessionViewModel_HiltModules;
import com.momokotle.agent.presentation.contacts.ContactsViewModel_HiltModules;
import com.momokotle.agent.presentation.custommenu.CustomFlowViewModel_HiltModules;
import com.momokotle.agent.presentation.dashboard.DashboardViewModel_HiltModules;
import com.momokotle.agent.presentation.orders.MyOrdersViewModel_HiltModules;
import com.momokotle.agent.presentation.orders.OrderDetailViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.CashInSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.CashOutSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.ProfitCalculationViewModel_HiltModules;
import com.momokotle.agent.presentation.referral.ReferralViewModel_HiltModules;
import com.momokotle.agent.presentation.registration.AgentFormViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.AccountProfileViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.AppSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.SettingsHomeViewModel_HiltModules;
import com.momokotle.agent.presentation.store.StoreViewModel_HiltModules;
import com.momokotle.agent.presentation.submissions.MySubmissionsViewModel_HiltModules;
import com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel_HiltModules;
import com.momokotle.agent.presentation.transactions.TransactionsViewModel_HiltModules;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.HiltWrapper_SavedStateHandleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class MomoAgentApplication_HiltComponents {
  private MomoAgentApplication_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          AppModule.class,
          ApplicationContextModule.class,
          FirebaseModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class,
          NetworkModule.class,
          RepositoryModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements MomoAgentApplication_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements MomoAccessibilityService_GeneratedInjector,
      ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AccountProfileViewModel_HiltModules.KeyModule.class,
          AgentFormViewModel_HiltModules.KeyModule.class,
          AppSettingsViewModel_HiltModules.KeyModule.class,
          CashInSettingsViewModel_HiltModules.KeyModule.class,
          CashOutSettingsViewModel_HiltModules.KeyModule.class,
          ContactsViewModel_HiltModules.KeyModule.class,
          CustomFlowViewModel_HiltModules.KeyModule.class,
          DashboardViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HiltWrapper_SavedStateHandleModule.class,
          LoginViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          MyOrdersViewModel_HiltModules.KeyModule.class,
          MySubmissionsViewModel_HiltModules.KeyModule.class,
          OrderDetailViewModel_HiltModules.KeyModule.class,
          ProfitCalculationViewModel_HiltModules.KeyModule.class,
          ReferralViewModel_HiltModules.KeyModule.class,
          RegisterViewModel_HiltModules.KeyModule.class,
          SessionViewModel_HiltModules.KeyModule.class,
          SettingsHomeViewModel_HiltModules.KeyModule.class,
          StoreViewModel_HiltModules.KeyModule.class,
          SubmissionDetailViewModel_HiltModules.KeyModule.class,
          TransactionsViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class,
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements MainActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AccountProfileViewModel_HiltModules.BindsModule.class,
          AgentFormViewModel_HiltModules.BindsModule.class,
          AppSettingsViewModel_HiltModules.BindsModule.class,
          CashInSettingsViewModel_HiltModules.BindsModule.class,
          CashOutSettingsViewModel_HiltModules.BindsModule.class,
          ContactsViewModel_HiltModules.BindsModule.class,
          CustomFlowViewModel_HiltModules.BindsModule.class,
          DashboardViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          LoginViewModel_HiltModules.BindsModule.class,
          MyOrdersViewModel_HiltModules.BindsModule.class,
          MySubmissionsViewModel_HiltModules.BindsModule.class,
          OrderDetailViewModel_HiltModules.BindsModule.class,
          ProfitCalculationViewModel_HiltModules.BindsModule.class,
          ReferralViewModel_HiltModules.BindsModule.class,
          RegisterViewModel_HiltModules.BindsModule.class,
          SessionViewModel_HiltModules.BindsModule.class,
          SettingsHomeViewModel_HiltModules.BindsModule.class,
          StoreViewModel_HiltModules.BindsModule.class,
          SubmissionDetailViewModel_HiltModules.BindsModule.class,
          TransactionsViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
