package com.momokotle.agent;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.momokotle.agent.accessibility.MomoAccessibilityService;
import com.momokotle.agent.accessibility.MomoAccessibilityService_MembersInjector;
import com.momokotle.agent.data.datastore.AuthTokenDataStore;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.data.datastore.UssdFlowCacheDataStore;
import com.momokotle.agent.data.local.DatabaseInitializer;
import com.momokotle.agent.data.remote.AuthInterceptor;
import com.momokotle.agent.data.remote.LaravelApplicationRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelBusinessSettingsRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelNotificationRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelOrderRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelProductRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelReferralRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelSubscriptionPlanRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelSubscriptionRemoteDataSource;
import com.momokotle.agent.data.remote.LaravelWalletRemoteDataSource;
import com.momokotle.agent.data.remote.MStorepayApiService;
import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.CustomFlowDao;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.database.dao.ProfitRuleDao;
import com.momokotle.agent.database.dao.SavedContactDao;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.database.dao.StoreProductDao;
import com.momokotle.agent.database.dao.SystemUssdFlowDao;
import com.momokotle.agent.database.dao.TransactionDao;
import com.momokotle.agent.di.AppModule_ProvideAccessibilityRepositoryFactory;
import com.momokotle.agent.di.AppModule_ProvideApplicationScopeFactory;
import com.momokotle.agent.di.AppModule_ProvideCustomFlowDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideDatabaseFactory;
import com.momokotle.agent.di.AppModule_ProvideFormSubmissionDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideProfitRuleDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideSavedContactDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideStoreOrderDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideStoreProductDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideSystemUssdFlowDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideTransactionDaoFactory;
import com.momokotle.agent.di.AppModule_ProvideUssdAutomationEngineFactory;
import com.momokotle.agent.di.AppModule_ProvideUssdDialHelperFactory;
import com.momokotle.agent.di.NetworkModule_ProvideGsonFactory;
import com.momokotle.agent.di.NetworkModule_ProvideMStorepayApiFactory;
import com.momokotle.agent.di.NetworkModule_ProvideOkHttpClientFactory;
import com.momokotle.agent.di.NetworkModule_ProvideRetrofitFactory;
import com.momokotle.agent.domain.engine.AgentCommissionEngine;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase;
import com.momokotle.agent.domain.usecase.CheckDuplicateTransactionUseCase;
import com.momokotle.agent.domain.usecase.CheckSubscriptionPaymentStatusUseCase;
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase;
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase;
import com.momokotle.agent.domain.usecase.InitializeManualSubscriptionPaymentUseCase;
import com.momokotle.agent.domain.usecase.InitializeSubscriptionPaymentUseCase;
import com.momokotle.agent.domain.usecase.LoginUseCase;
import com.momokotle.agent.domain.usecase.LogoutUseCase;
import com.momokotle.agent.domain.usecase.MarkAllNotificationsReadUseCase;
import com.momokotle.agent.domain.usecase.MarkNotificationReadUseCase;
import com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase;
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase;
import com.momokotle.agent.domain.usecase.ObserveCommissionSummaryUseCase;
import com.momokotle.agent.domain.usecase.ObserveNotificationsUseCase;
import com.momokotle.agent.domain.usecase.ObserveReferralInfoUseCase;
import com.momokotle.agent.domain.usecase.ObserveReferralsUseCase;
import com.momokotle.agent.domain.usecase.ObserveSubscriptionPlansUseCase;
import com.momokotle.agent.domain.usecase.ObserveWalletTransactionsUseCase;
import com.momokotle.agent.domain.usecase.ObserveWalletUseCase;
import com.momokotle.agent.domain.usecase.ObserveWithdrawalsUseCase;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase;
import com.momokotle.agent.domain.usecase.RegisterUseCase;
import com.momokotle.agent.domain.usecase.RequestWithdrawalUseCase;
import com.momokotle.agent.domain.usecase.ResendRegistrationOtpUseCase;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase;
import com.momokotle.agent.domain.usecase.VerifyRegistrationOtpUseCase;
import com.momokotle.agent.domain.ussd.EntitlementClock;
import com.momokotle.agent.presentation.MainActivity;
import com.momokotle.agent.presentation.auth.LoginViewModel;
import com.momokotle.agent.presentation.auth.LoginViewModel_HiltModules;
import com.momokotle.agent.presentation.auth.RegisterViewModel;
import com.momokotle.agent.presentation.auth.RegisterViewModel_HiltModules;
import com.momokotle.agent.presentation.auth.SessionViewModel;
import com.momokotle.agent.presentation.auth.SessionViewModel_HiltModules;
import com.momokotle.agent.presentation.contacts.ContactsViewModel;
import com.momokotle.agent.presentation.contacts.ContactsViewModel_HiltModules;
import com.momokotle.agent.presentation.custommenu.CustomFlowViewModel;
import com.momokotle.agent.presentation.custommenu.CustomFlowViewModel_HiltModules;
import com.momokotle.agent.presentation.dashboard.DashboardViewModel;
import com.momokotle.agent.presentation.dashboard.DashboardViewModel_HiltModules;
import com.momokotle.agent.presentation.notifications.NotificationsViewModel;
import com.momokotle.agent.presentation.notifications.NotificationsViewModel_HiltModules;
import com.momokotle.agent.presentation.orders.MyOrdersViewModel;
import com.momokotle.agent.presentation.orders.MyOrdersViewModel_HiltModules;
import com.momokotle.agent.presentation.orders.OrderDetailViewModel;
import com.momokotle.agent.presentation.orders.OrderDetailViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.CashInSettingsViewModel;
import com.momokotle.agent.presentation.profit.CashInSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.CashOutSettingsViewModel;
import com.momokotle.agent.presentation.profit.CashOutSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.profit.ProfitCalculationViewModel;
import com.momokotle.agent.presentation.profit.ProfitCalculationViewModel_HiltModules;
import com.momokotle.agent.presentation.referral.ReferralViewModel;
import com.momokotle.agent.presentation.referral.ReferralViewModel_HiltModules;
import com.momokotle.agent.presentation.registration.AgentFormViewModel;
import com.momokotle.agent.presentation.registration.AgentFormViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.AccountProfileViewModel;
import com.momokotle.agent.presentation.settings.AccountProfileViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.AppSettingsViewModel;
import com.momokotle.agent.presentation.settings.AppSettingsViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.HelpCenterViewModel;
import com.momokotle.agent.presentation.settings.HelpCenterViewModel_HiltModules;
import com.momokotle.agent.presentation.settings.SettingsHomeViewModel;
import com.momokotle.agent.presentation.settings.SettingsHomeViewModel_HiltModules;
import com.momokotle.agent.presentation.store.StoreViewModel;
import com.momokotle.agent.presentation.store.StoreViewModel_HiltModules;
import com.momokotle.agent.presentation.submissions.MySubmissionsViewModel;
import com.momokotle.agent.presentation.submissions.MySubmissionsViewModel_HiltModules;
import com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel;
import com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel_HiltModules;
import com.momokotle.agent.presentation.subscription.ManualPaymentViewModel;
import com.momokotle.agent.presentation.subscription.ManualPaymentViewModel_HiltModules;
import com.momokotle.agent.presentation.subscription.SubscriptionViewModel;
import com.momokotle.agent.presentation.subscription.SubscriptionViewModel_HiltModules;
import com.momokotle.agent.presentation.transactions.TransactionsViewModel;
import com.momokotle.agent.presentation.transactions.TransactionsViewModel_HiltModules;
import com.momokotle.agent.presentation.wallet.WalletViewModel;
import com.momokotle.agent.presentation.wallet.WalletViewModel_HiltModules;
import com.momokotle.agent.repository.AccessibilityRepositoryImpl;
import com.momokotle.agent.repository.AuthRepositoryImpl;
import com.momokotle.agent.repository.BusinessSettingsRepositoryImpl;
import com.momokotle.agent.repository.CustomFlowRepositoryImpl;
import com.momokotle.agent.repository.FormSubmissionRepositoryImpl;
import com.momokotle.agent.repository.NotificationRepositoryImpl;
import com.momokotle.agent.repository.OrderRepositoryImpl;
import com.momokotle.agent.repository.ProductRepositoryImpl;
import com.momokotle.agent.repository.ProfitRuleRepositoryImpl;
import com.momokotle.agent.repository.ReferralRepositoryImpl;
import com.momokotle.agent.repository.SavedContactRepositoryImpl;
import com.momokotle.agent.repository.StoreRepositoryImpl;
import com.momokotle.agent.repository.SubscriptionRepositoryImpl;
import com.momokotle.agent.repository.SystemUssdFlowRepositoryImpl;
import com.momokotle.agent.repository.TransactionRepositoryImpl;
import com.momokotle.agent.repository.WalletRepositoryImpl;
import com.momokotle.agent.sms.CashOutSmsWatcher;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdDialHelper;
import com.momokotle.agent.utils.AutomationLogger;
import com.momokotle.agent.utils.FileDocumentHelper;
import com.momokotle.agent.utils.NetworkMonitor;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerMomoAgentApplication_HiltComponents_SingletonC {
  private DaggerMomoAgentApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public MomoAgentApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements MomoAgentApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements MomoAgentApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements MomoAgentApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements MomoAgentApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements MomoAgentApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements MomoAgentApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements MomoAgentApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public MomoAgentApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends MomoAgentApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends MomoAgentApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends MomoAgentApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends MomoAgentApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(25).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_AccountProfileViewModel, AccountProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_registration_AgentFormViewModel, AgentFormViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_AppSettingsViewModel, AppSettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_CashInSettingsViewModel, CashInSettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_CashOutSettingsViewModel, CashOutSettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_contacts_ContactsViewModel, ContactsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_custommenu_CustomFlowViewModel, CustomFlowViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_HelpCenterViewModel, HelpCenterViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_subscription_ManualPaymentViewModel, ManualPaymentViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_orders_MyOrdersViewModel, MyOrdersViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_submissions_MySubmissionsViewModel, MySubmissionsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_notifications_NotificationsViewModel, NotificationsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_orders_OrderDetailViewModel, OrderDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_ProfitCalculationViewModel, ProfitCalculationViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_referral_ReferralViewModel, ReferralViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_RegisterViewModel, RegisterViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_SessionViewModel, SessionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_SettingsHomeViewModel, SettingsHomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_store_StoreViewModel, StoreViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel, SubmissionDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_subscription_SubscriptionViewModel, SubscriptionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_transactions_TransactionsViewModel, TransactionsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_momokotle_agent_presentation_wallet_WalletViewModel, WalletViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_momokotle_agent_presentation_referral_ReferralViewModel = "com.momokotle.agent.presentation.referral.ReferralViewModel";

      static String com_momokotle_agent_presentation_profit_CashInSettingsViewModel = "com.momokotle.agent.presentation.profit.CashInSettingsViewModel";

      static String com_momokotle_agent_presentation_profit_ProfitCalculationViewModel = "com.momokotle.agent.presentation.profit.ProfitCalculationViewModel";

      static String com_momokotle_agent_presentation_registration_AgentFormViewModel = "com.momokotle.agent.presentation.registration.AgentFormViewModel";

      static String com_momokotle_agent_presentation_settings_SettingsHomeViewModel = "com.momokotle.agent.presentation.settings.SettingsHomeViewModel";

      static String com_momokotle_agent_presentation_auth_RegisterViewModel = "com.momokotle.agent.presentation.auth.RegisterViewModel";

      static String com_momokotle_agent_presentation_auth_LoginViewModel = "com.momokotle.agent.presentation.auth.LoginViewModel";

      static String com_momokotle_agent_presentation_settings_HelpCenterViewModel = "com.momokotle.agent.presentation.settings.HelpCenterViewModel";

      static String com_momokotle_agent_presentation_wallet_WalletViewModel = "com.momokotle.agent.presentation.wallet.WalletViewModel";

      static String com_momokotle_agent_presentation_dashboard_DashboardViewModel = "com.momokotle.agent.presentation.dashboard.DashboardViewModel";

      static String com_momokotle_agent_presentation_orders_OrderDetailViewModel = "com.momokotle.agent.presentation.orders.OrderDetailViewModel";

      static String com_momokotle_agent_presentation_auth_SessionViewModel = "com.momokotle.agent.presentation.auth.SessionViewModel";

      static String com_momokotle_agent_presentation_transactions_TransactionsViewModel = "com.momokotle.agent.presentation.transactions.TransactionsViewModel";

      static String com_momokotle_agent_presentation_orders_MyOrdersViewModel = "com.momokotle.agent.presentation.orders.MyOrdersViewModel";

      static String com_momokotle_agent_presentation_submissions_MySubmissionsViewModel = "com.momokotle.agent.presentation.submissions.MySubmissionsViewModel";

      static String com_momokotle_agent_presentation_subscription_SubscriptionViewModel = "com.momokotle.agent.presentation.subscription.SubscriptionViewModel";

      static String com_momokotle_agent_presentation_notifications_NotificationsViewModel = "com.momokotle.agent.presentation.notifications.NotificationsViewModel";

      static String com_momokotle_agent_presentation_settings_AccountProfileViewModel = "com.momokotle.agent.presentation.settings.AccountProfileViewModel";

      static String com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel = "com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel";

      static String com_momokotle_agent_presentation_store_StoreViewModel = "com.momokotle.agent.presentation.store.StoreViewModel";

      static String com_momokotle_agent_presentation_subscription_ManualPaymentViewModel = "com.momokotle.agent.presentation.subscription.ManualPaymentViewModel";

      static String com_momokotle_agent_presentation_profit_CashOutSettingsViewModel = "com.momokotle.agent.presentation.profit.CashOutSettingsViewModel";

      static String com_momokotle_agent_presentation_contacts_ContactsViewModel = "com.momokotle.agent.presentation.contacts.ContactsViewModel";

      static String com_momokotle_agent_presentation_custommenu_CustomFlowViewModel = "com.momokotle.agent.presentation.custommenu.CustomFlowViewModel";

      static String com_momokotle_agent_presentation_settings_AppSettingsViewModel = "com.momokotle.agent.presentation.settings.AppSettingsViewModel";

      @KeepFieldType
      ReferralViewModel com_momokotle_agent_presentation_referral_ReferralViewModel2;

      @KeepFieldType
      CashInSettingsViewModel com_momokotle_agent_presentation_profit_CashInSettingsViewModel2;

      @KeepFieldType
      ProfitCalculationViewModel com_momokotle_agent_presentation_profit_ProfitCalculationViewModel2;

      @KeepFieldType
      AgentFormViewModel com_momokotle_agent_presentation_registration_AgentFormViewModel2;

      @KeepFieldType
      SettingsHomeViewModel com_momokotle_agent_presentation_settings_SettingsHomeViewModel2;

      @KeepFieldType
      RegisterViewModel com_momokotle_agent_presentation_auth_RegisterViewModel2;

      @KeepFieldType
      LoginViewModel com_momokotle_agent_presentation_auth_LoginViewModel2;

      @KeepFieldType
      HelpCenterViewModel com_momokotle_agent_presentation_settings_HelpCenterViewModel2;

      @KeepFieldType
      WalletViewModel com_momokotle_agent_presentation_wallet_WalletViewModel2;

      @KeepFieldType
      DashboardViewModel com_momokotle_agent_presentation_dashboard_DashboardViewModel2;

      @KeepFieldType
      OrderDetailViewModel com_momokotle_agent_presentation_orders_OrderDetailViewModel2;

      @KeepFieldType
      SessionViewModel com_momokotle_agent_presentation_auth_SessionViewModel2;

      @KeepFieldType
      TransactionsViewModel com_momokotle_agent_presentation_transactions_TransactionsViewModel2;

      @KeepFieldType
      MyOrdersViewModel com_momokotle_agent_presentation_orders_MyOrdersViewModel2;

      @KeepFieldType
      MySubmissionsViewModel com_momokotle_agent_presentation_submissions_MySubmissionsViewModel2;

      @KeepFieldType
      SubscriptionViewModel com_momokotle_agent_presentation_subscription_SubscriptionViewModel2;

      @KeepFieldType
      NotificationsViewModel com_momokotle_agent_presentation_notifications_NotificationsViewModel2;

      @KeepFieldType
      AccountProfileViewModel com_momokotle_agent_presentation_settings_AccountProfileViewModel2;

      @KeepFieldType
      SubmissionDetailViewModel com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel2;

      @KeepFieldType
      StoreViewModel com_momokotle_agent_presentation_store_StoreViewModel2;

      @KeepFieldType
      ManualPaymentViewModel com_momokotle_agent_presentation_subscription_ManualPaymentViewModel2;

      @KeepFieldType
      CashOutSettingsViewModel com_momokotle_agent_presentation_profit_CashOutSettingsViewModel2;

      @KeepFieldType
      ContactsViewModel com_momokotle_agent_presentation_contacts_ContactsViewModel2;

      @KeepFieldType
      CustomFlowViewModel com_momokotle_agent_presentation_custommenu_CustomFlowViewModel2;

      @KeepFieldType
      AppSettingsViewModel com_momokotle_agent_presentation_settings_AppSettingsViewModel2;
    }
  }

  private static final class ViewModelCImpl extends MomoAgentApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AccountProfileViewModel> accountProfileViewModelProvider;

    private Provider<AgentFormViewModel> agentFormViewModelProvider;

    private Provider<AppSettingsViewModel> appSettingsViewModelProvider;

    private Provider<CashInSettingsViewModel> cashInSettingsViewModelProvider;

    private Provider<CashOutSettingsViewModel> cashOutSettingsViewModelProvider;

    private Provider<ContactsViewModel> contactsViewModelProvider;

    private Provider<CustomFlowViewModel> customFlowViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<HelpCenterViewModel> helpCenterViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<ManualPaymentViewModel> manualPaymentViewModelProvider;

    private Provider<MyOrdersViewModel> myOrdersViewModelProvider;

    private Provider<MySubmissionsViewModel> mySubmissionsViewModelProvider;

    private Provider<NotificationsViewModel> notificationsViewModelProvider;

    private Provider<OrderDetailViewModel> orderDetailViewModelProvider;

    private Provider<ProfitCalculationViewModel> profitCalculationViewModelProvider;

    private Provider<ReferralViewModel> referralViewModelProvider;

    private Provider<RegisterViewModel> registerViewModelProvider;

    private Provider<SessionViewModel> sessionViewModelProvider;

    private Provider<SettingsHomeViewModel> settingsHomeViewModelProvider;

    private Provider<StoreViewModel> storeViewModelProvider;

    private Provider<SubmissionDetailViewModel> submissionDetailViewModelProvider;

    private Provider<SubscriptionViewModel> subscriptionViewModelProvider;

    private Provider<TransactionsViewModel> transactionsViewModelProvider;

    private Provider<WalletViewModel> walletViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase() {
      return new GetAuthenticatedUserUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private SaveContactUseCase saveContactUseCase() {
      return new SaveContactUseCase(singletonCImpl.savedContactRepositoryImplProvider.get());
    }

    private StartUssdFlowUseCase startUssdFlowUseCase() {
      return new StartUssdFlowUseCase(singletonCImpl.provideUssdDialHelperProvider.get(), singletonCImpl.provideUssdAutomationEngineProvider.get(), singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.systemUssdFlowRepositoryImplProvider.get(), singletonCImpl.cashOutSmsWatcherProvider.get());
    }

    private CheckUssdAccessUseCase checkUssdAccessUseCase() {
      return new CheckUssdAccessUseCase(singletonCImpl.userAccountDataStoreProvider.get(), singletonCImpl.entitlementClockProvider.get());
    }

    private RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase() {
      return new RecordAttemptedTransactionUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private CheckDuplicateTransactionUseCase checkDuplicateTransactionUseCase() {
      return new CheckDuplicateTransactionUseCase(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.settingsDataStoreProvider.get());
    }

    private RefreshUserProfileUseCase refreshUserProfileUseCase() {
      return new RefreshUserProfileUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private ObserveCommissionSummaryUseCase observeCommissionSummaryUseCase() {
      return new ObserveCommissionSummaryUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private ObserveBusinessSettingsUseCase observeBusinessSettingsUseCase() {
      return new ObserveBusinessSettingsUseCase(singletonCImpl.businessSettingsRepositoryImplProvider.get());
    }

    private LoginUseCase loginUseCase() {
      return new LoginUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private ObserveSubscriptionPlansUseCase observeSubscriptionPlansUseCase() {
      return new ObserveSubscriptionPlansUseCase(singletonCImpl.subscriptionRepositoryImplProvider.get());
    }

    private InitializeManualSubscriptionPaymentUseCase initializeManualSubscriptionPaymentUseCase(
        ) {
      return new InitializeManualSubscriptionPaymentUseCase(singletonCImpl.subscriptionRepositoryImplProvider.get());
    }

    private CheckSubscriptionPaymentStatusUseCase checkSubscriptionPaymentStatusUseCase() {
      return new CheckSubscriptionPaymentStatusUseCase(singletonCImpl.subscriptionRepositoryImplProvider.get());
    }

    private ObserveNotificationsUseCase observeNotificationsUseCase() {
      return new ObserveNotificationsUseCase(singletonCImpl.notificationRepositoryImplProvider.get());
    }

    private MarkNotificationReadUseCase markNotificationReadUseCase() {
      return new MarkNotificationReadUseCase(singletonCImpl.notificationRepositoryImplProvider.get());
    }

    private MarkAllNotificationsReadUseCase markAllNotificationsReadUseCase() {
      return new MarkAllNotificationsReadUseCase(singletonCImpl.notificationRepositoryImplProvider.get());
    }

    private ObserveReferralInfoUseCase observeReferralInfoUseCase() {
      return new ObserveReferralInfoUseCase(singletonCImpl.referralRepositoryImplProvider.get());
    }

    private ObserveReferralsUseCase observeReferralsUseCase() {
      return new ObserveReferralsUseCase(singletonCImpl.referralRepositoryImplProvider.get());
    }

    private ObserveWalletTransactionsUseCase observeWalletTransactionsUseCase() {
      return new ObserveWalletTransactionsUseCase(singletonCImpl.walletRepositoryImplProvider.get());
    }

    private RegisterUseCase registerUseCase() {
      return new RegisterUseCase(singletonCImpl.authRepositoryImplProvider.get(), singletonCImpl.referralRepositoryImplProvider.get());
    }

    private VerifyRegistrationOtpUseCase verifyRegistrationOtpUseCase() {
      return new VerifyRegistrationOtpUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private ResendRegistrationOtpUseCase resendRegistrationOtpUseCase() {
      return new ResendRegistrationOtpUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private ObserveAuthStateUseCase observeAuthStateUseCase() {
      return new ObserveAuthStateUseCase(singletonCImpl.authRepositoryImplProvider.get());
    }

    private LogoutUseCase logoutUseCase() {
      return new LogoutUseCase(singletonCImpl.authRepositoryImplProvider.get(), singletonCImpl.productRepositoryImplProvider.get(), singletonCImpl.orderRepositoryImplProvider.get(), singletonCImpl.formSubmissionRepositoryImplProvider.get());
    }

    private InitializeSubscriptionPaymentUseCase initializeSubscriptionPaymentUseCase() {
      return new InitializeSubscriptionPaymentUseCase(singletonCImpl.subscriptionRepositoryImplProvider.get());
    }

    private ObserveWalletUseCase observeWalletUseCase() {
      return new ObserveWalletUseCase(singletonCImpl.walletRepositoryImplProvider.get());
    }

    private ObserveWithdrawalsUseCase observeWithdrawalsUseCase() {
      return new ObserveWithdrawalsUseCase(singletonCImpl.walletRepositoryImplProvider.get());
    }

    private RequestWithdrawalUseCase requestWithdrawalUseCase() {
      return new RequestWithdrawalUseCase(singletonCImpl.walletRepositoryImplProvider.get(), singletonCImpl.businessSettingsRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.accountProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.agentFormViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.appSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.cashInSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.cashOutSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.contactsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.customFlowViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.helpCenterViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.manualPaymentViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.myOrdersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.mySubmissionsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.notificationsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.orderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.profitCalculationViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.referralViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.registerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
      this.sessionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 18);
      this.settingsHomeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 19);
      this.storeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 20);
      this.submissionDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 21);
      this.subscriptionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 22);
      this.transactionsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 23);
      this.walletViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 24);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(25).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_AccountProfileViewModel, ((Provider) accountProfileViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_registration_AgentFormViewModel, ((Provider) agentFormViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_AppSettingsViewModel, ((Provider) appSettingsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_CashInSettingsViewModel, ((Provider) cashInSettingsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_CashOutSettingsViewModel, ((Provider) cashOutSettingsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_contacts_ContactsViewModel, ((Provider) contactsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_custommenu_CustomFlowViewModel, ((Provider) customFlowViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_HelpCenterViewModel, ((Provider) helpCenterViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_subscription_ManualPaymentViewModel, ((Provider) manualPaymentViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_orders_MyOrdersViewModel, ((Provider) myOrdersViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_submissions_MySubmissionsViewModel, ((Provider) mySubmissionsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_notifications_NotificationsViewModel, ((Provider) notificationsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_orders_OrderDetailViewModel, ((Provider) orderDetailViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_profit_ProfitCalculationViewModel, ((Provider) profitCalculationViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_referral_ReferralViewModel, ((Provider) referralViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_RegisterViewModel, ((Provider) registerViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_auth_SessionViewModel, ((Provider) sessionViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_settings_SettingsHomeViewModel, ((Provider) settingsHomeViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_store_StoreViewModel, ((Provider) storeViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel, ((Provider) submissionDetailViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_subscription_SubscriptionViewModel, ((Provider) subscriptionViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_transactions_TransactionsViewModel, ((Provider) transactionsViewModelProvider)).put(LazyClassKeyProvider.com_momokotle_agent_presentation_wallet_WalletViewModel, ((Provider) walletViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_momokotle_agent_presentation_auth_LoginViewModel = "com.momokotle.agent.presentation.auth.LoginViewModel";

      static String com_momokotle_agent_presentation_auth_SessionViewModel = "com.momokotle.agent.presentation.auth.SessionViewModel";

      static String com_momokotle_agent_presentation_settings_AccountProfileViewModel = "com.momokotle.agent.presentation.settings.AccountProfileViewModel";

      static String com_momokotle_agent_presentation_notifications_NotificationsViewModel = "com.momokotle.agent.presentation.notifications.NotificationsViewModel";

      static String com_momokotle_agent_presentation_profit_ProfitCalculationViewModel = "com.momokotle.agent.presentation.profit.ProfitCalculationViewModel";

      static String com_momokotle_agent_presentation_store_StoreViewModel = "com.momokotle.agent.presentation.store.StoreViewModel";

      static String com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel = "com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel";

      static String com_momokotle_agent_presentation_contacts_ContactsViewModel = "com.momokotle.agent.presentation.contacts.ContactsViewModel";

      static String com_momokotle_agent_presentation_profit_CashInSettingsViewModel = "com.momokotle.agent.presentation.profit.CashInSettingsViewModel";

      static String com_momokotle_agent_presentation_orders_MyOrdersViewModel = "com.momokotle.agent.presentation.orders.MyOrdersViewModel";

      static String com_momokotle_agent_presentation_custommenu_CustomFlowViewModel = "com.momokotle.agent.presentation.custommenu.CustomFlowViewModel";

      static String com_momokotle_agent_presentation_submissions_MySubmissionsViewModel = "com.momokotle.agent.presentation.submissions.MySubmissionsViewModel";

      static String com_momokotle_agent_presentation_profit_CashOutSettingsViewModel = "com.momokotle.agent.presentation.profit.CashOutSettingsViewModel";

      static String com_momokotle_agent_presentation_settings_SettingsHomeViewModel = "com.momokotle.agent.presentation.settings.SettingsHomeViewModel";

      static String com_momokotle_agent_presentation_dashboard_DashboardViewModel = "com.momokotle.agent.presentation.dashboard.DashboardViewModel";

      static String com_momokotle_agent_presentation_subscription_ManualPaymentViewModel = "com.momokotle.agent.presentation.subscription.ManualPaymentViewModel";

      static String com_momokotle_agent_presentation_referral_ReferralViewModel = "com.momokotle.agent.presentation.referral.ReferralViewModel";

      static String com_momokotle_agent_presentation_orders_OrderDetailViewModel = "com.momokotle.agent.presentation.orders.OrderDetailViewModel";

      static String com_momokotle_agent_presentation_registration_AgentFormViewModel = "com.momokotle.agent.presentation.registration.AgentFormViewModel";

      static String com_momokotle_agent_presentation_settings_HelpCenterViewModel = "com.momokotle.agent.presentation.settings.HelpCenterViewModel";

      static String com_momokotle_agent_presentation_subscription_SubscriptionViewModel = "com.momokotle.agent.presentation.subscription.SubscriptionViewModel";

      static String com_momokotle_agent_presentation_wallet_WalletViewModel = "com.momokotle.agent.presentation.wallet.WalletViewModel";

      static String com_momokotle_agent_presentation_transactions_TransactionsViewModel = "com.momokotle.agent.presentation.transactions.TransactionsViewModel";

      static String com_momokotle_agent_presentation_auth_RegisterViewModel = "com.momokotle.agent.presentation.auth.RegisterViewModel";

      static String com_momokotle_agent_presentation_settings_AppSettingsViewModel = "com.momokotle.agent.presentation.settings.AppSettingsViewModel";

      @KeepFieldType
      LoginViewModel com_momokotle_agent_presentation_auth_LoginViewModel2;

      @KeepFieldType
      SessionViewModel com_momokotle_agent_presentation_auth_SessionViewModel2;

      @KeepFieldType
      AccountProfileViewModel com_momokotle_agent_presentation_settings_AccountProfileViewModel2;

      @KeepFieldType
      NotificationsViewModel com_momokotle_agent_presentation_notifications_NotificationsViewModel2;

      @KeepFieldType
      ProfitCalculationViewModel com_momokotle_agent_presentation_profit_ProfitCalculationViewModel2;

      @KeepFieldType
      StoreViewModel com_momokotle_agent_presentation_store_StoreViewModel2;

      @KeepFieldType
      SubmissionDetailViewModel com_momokotle_agent_presentation_submissions_SubmissionDetailViewModel2;

      @KeepFieldType
      ContactsViewModel com_momokotle_agent_presentation_contacts_ContactsViewModel2;

      @KeepFieldType
      CashInSettingsViewModel com_momokotle_agent_presentation_profit_CashInSettingsViewModel2;

      @KeepFieldType
      MyOrdersViewModel com_momokotle_agent_presentation_orders_MyOrdersViewModel2;

      @KeepFieldType
      CustomFlowViewModel com_momokotle_agent_presentation_custommenu_CustomFlowViewModel2;

      @KeepFieldType
      MySubmissionsViewModel com_momokotle_agent_presentation_submissions_MySubmissionsViewModel2;

      @KeepFieldType
      CashOutSettingsViewModel com_momokotle_agent_presentation_profit_CashOutSettingsViewModel2;

      @KeepFieldType
      SettingsHomeViewModel com_momokotle_agent_presentation_settings_SettingsHomeViewModel2;

      @KeepFieldType
      DashboardViewModel com_momokotle_agent_presentation_dashboard_DashboardViewModel2;

      @KeepFieldType
      ManualPaymentViewModel com_momokotle_agent_presentation_subscription_ManualPaymentViewModel2;

      @KeepFieldType
      ReferralViewModel com_momokotle_agent_presentation_referral_ReferralViewModel2;

      @KeepFieldType
      OrderDetailViewModel com_momokotle_agent_presentation_orders_OrderDetailViewModel2;

      @KeepFieldType
      AgentFormViewModel com_momokotle_agent_presentation_registration_AgentFormViewModel2;

      @KeepFieldType
      HelpCenterViewModel com_momokotle_agent_presentation_settings_HelpCenterViewModel2;

      @KeepFieldType
      SubscriptionViewModel com_momokotle_agent_presentation_subscription_SubscriptionViewModel2;

      @KeepFieldType
      WalletViewModel com_momokotle_agent_presentation_wallet_WalletViewModel2;

      @KeepFieldType
      TransactionsViewModel com_momokotle_agent_presentation_transactions_TransactionsViewModel2;

      @KeepFieldType
      RegisterViewModel com_momokotle_agent_presentation_auth_RegisterViewModel2;

      @KeepFieldType
      AppSettingsViewModel com_momokotle_agent_presentation_settings_AppSettingsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.momokotle.agent.presentation.settings.AccountProfileViewModel 
          return (T) new AccountProfileViewModel(singletonCImpl.userAccountDataStoreProvider.get());

          case 1: // com.momokotle.agent.presentation.registration.AgentFormViewModel 
          return (T) new AgentFormViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.formSubmissionRepositoryImplProvider.get(), singletonCImpl.productRepositoryImplProvider.get(), singletonCImpl.storeRepositoryImplProvider.get(), viewModelCImpl.getAuthenticatedUserUseCase(), singletonCImpl.networkMonitorProvider.get(), singletonCImpl.fileDocumentHelperProvider.get());

          case 2: // com.momokotle.agent.presentation.settings.AppSettingsViewModel 
          return (T) new AppSettingsViewModel(singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.provideAccessibilityRepositoryProvider.get());

          case 3: // com.momokotle.agent.presentation.profit.CashInSettingsViewModel 
          return (T) new CashInSettingsViewModel(singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.profitRuleRepositoryImplProvider.get());

          case 4: // com.momokotle.agent.presentation.profit.CashOutSettingsViewModel 
          return (T) new CashOutSettingsViewModel(singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.profitRuleRepositoryImplProvider.get());

          case 5: // com.momokotle.agent.presentation.contacts.ContactsViewModel 
          return (T) new ContactsViewModel(singletonCImpl.savedContactRepositoryImplProvider.get(), viewModelCImpl.saveContactUseCase());

          case 6: // com.momokotle.agent.presentation.custommenu.CustomFlowViewModel 
          return (T) new CustomFlowViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.startUssdFlowUseCase(), singletonCImpl.provideUssdAutomationEngineProvider.get(), singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.provideAccessibilityRepositoryProvider.get(), viewModelCImpl.checkUssdAccessUseCase(), singletonCImpl.cashOutSmsWatcherProvider.get(), singletonCImpl.customFlowRepositoryImplProvider.get(), viewModelCImpl.recordAttemptedTransactionUseCase(), singletonCImpl.businessSettingsRepositoryImplProvider.get(), viewModelCImpl.checkDuplicateTransactionUseCase());

          case 7: // com.momokotle.agent.presentation.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.startUssdFlowUseCase(), singletonCImpl.provideUssdAutomationEngineProvider.get(), singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.provideAccessibilityRepositoryProvider.get(), viewModelCImpl.checkUssdAccessUseCase(), singletonCImpl.cashOutSmsWatcherProvider.get(), viewModelCImpl.refreshUserProfileUseCase(), viewModelCImpl.recordAttemptedTransactionUseCase(), singletonCImpl.transactionRepositoryImplProvider.get(), viewModelCImpl.observeCommissionSummaryUseCase(), singletonCImpl.customFlowRepositoryImplProvider.get(), singletonCImpl.systemUssdFlowRepositoryImplProvider.get(), singletonCImpl.savedContactRepositoryImplProvider.get(), viewModelCImpl.saveContactUseCase(), singletonCImpl.businessSettingsRepositoryImplProvider.get(), viewModelCImpl.checkDuplicateTransactionUseCase());

          case 8: // com.momokotle.agent.presentation.settings.HelpCenterViewModel 
          return (T) new HelpCenterViewModel(viewModelCImpl.observeBusinessSettingsUseCase());

          case 9: // com.momokotle.agent.presentation.auth.LoginViewModel 
          return (T) new LoginViewModel(viewModelCImpl.loginUseCase());

          case 10: // com.momokotle.agent.presentation.subscription.ManualPaymentViewModel 
          return (T) new ManualPaymentViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.observeSubscriptionPlansUseCase(), viewModelCImpl.observeBusinessSettingsUseCase(), viewModelCImpl.initializeManualSubscriptionPaymentUseCase(), viewModelCImpl.checkSubscriptionPaymentStatusUseCase(), viewModelCImpl.refreshUserProfileUseCase(), singletonCImpl.networkMonitorProvider.get());

          case 11: // com.momokotle.agent.presentation.orders.MyOrdersViewModel 
          return (T) new MyOrdersViewModel(singletonCImpl.authRepositoryImplProvider.get(), singletonCImpl.orderRepositoryImplProvider.get());

          case 12: // com.momokotle.agent.presentation.submissions.MySubmissionsViewModel 
          return (T) new MySubmissionsViewModel(singletonCImpl.authRepositoryImplProvider.get(), singletonCImpl.formSubmissionRepositoryImplProvider.get());

          case 13: // com.momokotle.agent.presentation.notifications.NotificationsViewModel 
          return (T) new NotificationsViewModel(singletonCImpl.authRepositoryImplProvider.get(), viewModelCImpl.observeNotificationsUseCase(), viewModelCImpl.markNotificationReadUseCase(), viewModelCImpl.markAllNotificationsReadUseCase());

          case 14: // com.momokotle.agent.presentation.orders.OrderDetailViewModel 
          return (T) new OrderDetailViewModel(singletonCImpl.orderRepositoryImplProvider.get());

          case 15: // com.momokotle.agent.presentation.profit.ProfitCalculationViewModel 
          return (T) new ProfitCalculationViewModel(singletonCImpl.settingsDataStoreProvider.get(), singletonCImpl.calculateVendorProfitUseCase());

          case 16: // com.momokotle.agent.presentation.referral.ReferralViewModel 
          return (T) new ReferralViewModel(singletonCImpl.authRepositoryImplProvider.get(), viewModelCImpl.observeReferralInfoUseCase(), viewModelCImpl.observeReferralsUseCase(), viewModelCImpl.observeWalletTransactionsUseCase());

          case 17: // com.momokotle.agent.presentation.auth.RegisterViewModel 
          return (T) new RegisterViewModel(viewModelCImpl.registerUseCase(), viewModelCImpl.verifyRegistrationOtpUseCase(), viewModelCImpl.resendRegistrationOtpUseCase());

          case 18: // com.momokotle.agent.presentation.auth.SessionViewModel 
          return (T) new SessionViewModel(viewModelCImpl.observeAuthStateUseCase(), viewModelCImpl.refreshUserProfileUseCase(), singletonCImpl.authRepositoryImplProvider.get(), singletonCImpl.productRepositoryImplProvider.get(), singletonCImpl.orderRepositoryImplProvider.get(), singletonCImpl.formSubmissionRepositoryImplProvider.get());

          case 19: // com.momokotle.agent.presentation.settings.SettingsHomeViewModel 
          return (T) new SettingsHomeViewModel(singletonCImpl.userAccountDataStoreProvider.get(), viewModelCImpl.logoutUseCase(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 20: // com.momokotle.agent.presentation.store.StoreViewModel 
          return (T) new StoreViewModel(singletonCImpl.networkMonitorProvider.get(), singletonCImpl.productRepositoryImplProvider.get(), singletonCImpl.storeRepositoryImplProvider.get(), viewModelCImpl.getAuthenticatedUserUseCase(), viewModelCImpl.observeAuthStateUseCase());

          case 21: // com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel 
          return (T) new SubmissionDetailViewModel(singletonCImpl.formSubmissionRepositoryImplProvider.get());

          case 22: // com.momokotle.agent.presentation.subscription.SubscriptionViewModel 
          return (T) new SubscriptionViewModel(singletonCImpl.authRepositoryImplProvider.get(), viewModelCImpl.observeSubscriptionPlansUseCase(), viewModelCImpl.observeBusinessSettingsUseCase(), viewModelCImpl.initializeSubscriptionPaymentUseCase(), viewModelCImpl.checkSubscriptionPaymentStatusUseCase(), viewModelCImpl.refreshUserProfileUseCase(), singletonCImpl.networkMonitorProvider.get());

          case 23: // com.momokotle.agent.presentation.transactions.TransactionsViewModel 
          return (T) new TransactionsViewModel(singletonCImpl.transactionRepositoryImplProvider.get(), viewModelCImpl.observeCommissionSummaryUseCase());

          case 24: // com.momokotle.agent.presentation.wallet.WalletViewModel 
          return (T) new WalletViewModel(singletonCImpl.authRepositoryImplProvider.get(), viewModelCImpl.observeWalletUseCase(), viewModelCImpl.observeWalletTransactionsUseCase(), viewModelCImpl.observeWithdrawalsUseCase(), viewModelCImpl.requestWithdrawalUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends MomoAgentApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends MomoAgentApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectMomoAccessibilityService(MomoAccessibilityService momoAccessibilityService) {
      injectMomoAccessibilityService2(momoAccessibilityService);
    }

    private MomoAccessibilityService injectMomoAccessibilityService2(
        MomoAccessibilityService instance) {
      MomoAccessibilityService_MembersInjector.injectEngine(instance, singletonCImpl.provideUssdAutomationEngineProvider.get());
      MomoAccessibilityService_MembersInjector.injectLogger(instance, singletonCImpl.automationLoggerProvider.get());
      MomoAccessibilityService_MembersInjector.injectUpdateTransactionStatusUseCase(instance, singletonCImpl.updateTransactionStatusUseCase());
      MomoAccessibilityService_MembersInjector.injectAccessibilityRepository(instance, singletonCImpl.provideAccessibilityRepositoryProvider.get());
      MomoAccessibilityService_MembersInjector.injectCashOutSmsWatcher(instance, singletonCImpl.cashOutSmsWatcherProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends MomoAgentApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<DatabaseInitializer> databaseInitializerProvider;

    private Provider<UserAccountDataStore> userAccountDataStoreProvider;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<AuthTokenDataStore> authTokenDataStoreProvider;

    private Provider<AuthInterceptor> authInterceptorProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<MStorepayApiService> provideMStorepayApiProvider;

    private Provider<LaravelApplicationRemoteDataSource> laravelApplicationRemoteDataSourceProvider;

    private Provider<NetworkMonitor> networkMonitorProvider;

    private Provider<CoroutineScope> provideApplicationScopeProvider;

    private Provider<FormSubmissionRepositoryImpl> formSubmissionRepositoryImplProvider;

    private Provider<LaravelProductRemoteDataSource> laravelProductRemoteDataSourceProvider;

    private Provider<ProductRepositoryImpl> productRepositoryImplProvider;

    private Provider<LaravelOrderRemoteDataSource> laravelOrderRemoteDataSourceProvider;

    private Provider<OrderRepositoryImpl> orderRepositoryImplProvider;

    private Provider<StoreRepositoryImpl> storeRepositoryImplProvider;

    private Provider<AuthRepositoryImpl> authRepositoryImplProvider;

    private Provider<FileDocumentHelper> fileDocumentHelperProvider;

    private Provider<SettingsDataStore> settingsDataStoreProvider;

    private Provider<AccessibilityRepositoryImpl> accessibilityRepositoryImplProvider;

    private Provider<AccessibilityRepository> provideAccessibilityRepositoryProvider;

    private Provider<ProfitRuleRepositoryImpl> profitRuleRepositoryImplProvider;

    private Provider<SavedContactRepositoryImpl> savedContactRepositoryImplProvider;

    private Provider<UssdDialHelper> provideUssdDialHelperProvider;

    private Provider<AutomationLogger> automationLoggerProvider;

    private Provider<UssdAutomationEngine> provideUssdAutomationEngineProvider;

    private Provider<Gson> provideGsonProvider;

    private Provider<UssdFlowCacheDataStore> ussdFlowCacheDataStoreProvider;

    private Provider<SystemUssdFlowRepositoryImpl> systemUssdFlowRepositoryImplProvider;

    private Provider<TransactionRepositoryImpl> transactionRepositoryImplProvider;

    private Provider<AgentCommissionEngine> agentCommissionEngineProvider;

    private Provider<CashOutSmsWatcher> cashOutSmsWatcherProvider;

    private Provider<EntitlementClock> entitlementClockProvider;

    private Provider<CustomFlowRepositoryImpl> customFlowRepositoryImplProvider;

    private Provider<LaravelBusinessSettingsRemoteDataSource> laravelBusinessSettingsRemoteDataSourceProvider;

    private Provider<BusinessSettingsRepositoryImpl> businessSettingsRepositoryImplProvider;

    private Provider<LaravelSubscriptionPlanRemoteDataSource> laravelSubscriptionPlanRemoteDataSourceProvider;

    private Provider<LaravelSubscriptionRemoteDataSource> laravelSubscriptionRemoteDataSourceProvider;

    private Provider<SubscriptionRepositoryImpl> subscriptionRepositoryImplProvider;

    private Provider<LaravelNotificationRemoteDataSource> laravelNotificationRemoteDataSourceProvider;

    private Provider<NotificationRepositoryImpl> notificationRepositoryImplProvider;

    private Provider<LaravelReferralRemoteDataSource> laravelReferralRemoteDataSourceProvider;

    private Provider<ReferralRepositoryImpl> referralRepositoryImplProvider;

    private Provider<LaravelWalletRemoteDataSource> laravelWalletRemoteDataSourceProvider;

    private Provider<WalletRepositoryImpl> walletRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private FormSubmissionDao formSubmissionDao() {
      return AppModule_ProvideFormSubmissionDaoFactory.provideFormSubmissionDao(provideDatabaseProvider.get());
    }

    private StoreProductDao storeProductDao() {
      return AppModule_ProvideStoreProductDaoFactory.provideStoreProductDao(provideDatabaseProvider.get());
    }

    private StoreOrderDao storeOrderDao() {
      return AppModule_ProvideStoreOrderDaoFactory.provideStoreOrderDao(provideDatabaseProvider.get());
    }

    private ProfitRuleDao profitRuleDao() {
      return AppModule_ProvideProfitRuleDaoFactory.provideProfitRuleDao(provideDatabaseProvider.get());
    }

    private SavedContactDao savedContactDao() {
      return AppModule_ProvideSavedContactDaoFactory.provideSavedContactDao(provideDatabaseProvider.get());
    }

    private SystemUssdFlowDao systemUssdFlowDao() {
      return AppModule_ProvideSystemUssdFlowDaoFactory.provideSystemUssdFlowDao(provideDatabaseProvider.get());
    }

    private TransactionDao transactionDao() {
      return AppModule_ProvideTransactionDaoFactory.provideTransactionDao(provideDatabaseProvider.get());
    }

    private CalculateVendorProfitUseCase calculateVendorProfitUseCase() {
      return new CalculateVendorProfitUseCase(agentCommissionEngineProvider.get(), profitRuleRepositoryImplProvider.get(), settingsDataStoreProvider.get());
    }

    private UpdateTransactionStatusUseCase updateTransactionStatusUseCase() {
      return new UpdateTransactionStatusUseCase(transactionRepositoryImplProvider.get(), calculateVendorProfitUseCase(), settingsDataStoreProvider.get());
    }

    private CustomFlowDao customFlowDao() {
      return AppModule_ProvideCustomFlowDaoFactory.provideCustomFlowDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.databaseInitializerProvider = DoubleCheck.provider(new SwitchingProvider<DatabaseInitializer>(singletonCImpl, 0));
      this.userAccountDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<UserAccountDataStore>(singletonCImpl, 1));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 3));
      this.authTokenDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<AuthTokenDataStore>(singletonCImpl, 9));
      this.authInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<AuthInterceptor>(singletonCImpl, 8));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 7));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 6));
      this.provideMStorepayApiProvider = DoubleCheck.provider(new SwitchingProvider<MStorepayApiService>(singletonCImpl, 5));
      this.laravelApplicationRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelApplicationRemoteDataSource>(singletonCImpl, 4));
      this.networkMonitorProvider = DoubleCheck.provider(new SwitchingProvider<NetworkMonitor>(singletonCImpl, 10));
      this.provideApplicationScopeProvider = DoubleCheck.provider(new SwitchingProvider<CoroutineScope>(singletonCImpl, 11));
      this.formSubmissionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<FormSubmissionRepositoryImpl>(singletonCImpl, 2));
      this.laravelProductRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelProductRemoteDataSource>(singletonCImpl, 13));
      this.productRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProductRepositoryImpl>(singletonCImpl, 12));
      this.laravelOrderRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelOrderRemoteDataSource>(singletonCImpl, 16));
      this.orderRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<OrderRepositoryImpl>(singletonCImpl, 15));
      this.storeRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<StoreRepositoryImpl>(singletonCImpl, 14));
      this.authRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepositoryImpl>(singletonCImpl, 17));
      this.fileDocumentHelperProvider = DoubleCheck.provider(new SwitchingProvider<FileDocumentHelper>(singletonCImpl, 18));
      this.settingsDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<SettingsDataStore>(singletonCImpl, 19));
      this.accessibilityRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AccessibilityRepositoryImpl>(singletonCImpl, 21));
      this.provideAccessibilityRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AccessibilityRepository>(singletonCImpl, 20));
      this.profitRuleRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProfitRuleRepositoryImpl>(singletonCImpl, 22));
      this.savedContactRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SavedContactRepositoryImpl>(singletonCImpl, 23));
      this.provideUssdDialHelperProvider = DoubleCheck.provider(new SwitchingProvider<UssdDialHelper>(singletonCImpl, 24));
      this.automationLoggerProvider = DoubleCheck.provider(new SwitchingProvider<AutomationLogger>(singletonCImpl, 26));
      this.provideUssdAutomationEngineProvider = DoubleCheck.provider(new SwitchingProvider<UssdAutomationEngine>(singletonCImpl, 25));
      this.provideGsonProvider = DoubleCheck.provider(new SwitchingProvider<Gson>(singletonCImpl, 29));
      this.ussdFlowCacheDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<UssdFlowCacheDataStore>(singletonCImpl, 28));
      this.systemUssdFlowRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SystemUssdFlowRepositoryImpl>(singletonCImpl, 27));
      this.transactionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TransactionRepositoryImpl>(singletonCImpl, 31));
      this.agentCommissionEngineProvider = DoubleCheck.provider(new SwitchingProvider<AgentCommissionEngine>(singletonCImpl, 32));
      this.cashOutSmsWatcherProvider = DoubleCheck.provider(new SwitchingProvider<CashOutSmsWatcher>(singletonCImpl, 30));
      this.entitlementClockProvider = DoubleCheck.provider(new SwitchingProvider<EntitlementClock>(singletonCImpl, 33));
      this.customFlowRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CustomFlowRepositoryImpl>(singletonCImpl, 34));
      this.laravelBusinessSettingsRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelBusinessSettingsRemoteDataSource>(singletonCImpl, 36));
      this.businessSettingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<BusinessSettingsRepositoryImpl>(singletonCImpl, 35));
      this.laravelSubscriptionPlanRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelSubscriptionPlanRemoteDataSource>(singletonCImpl, 38));
      this.laravelSubscriptionRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelSubscriptionRemoteDataSource>(singletonCImpl, 39));
      this.subscriptionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SubscriptionRepositoryImpl>(singletonCImpl, 37));
      this.laravelNotificationRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelNotificationRemoteDataSource>(singletonCImpl, 41));
      this.notificationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<NotificationRepositoryImpl>(singletonCImpl, 40));
      this.laravelReferralRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelReferralRemoteDataSource>(singletonCImpl, 43));
      this.referralRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ReferralRepositoryImpl>(singletonCImpl, 42));
      this.laravelWalletRemoteDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<LaravelWalletRemoteDataSource>(singletonCImpl, 45));
      this.walletRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<WalletRepositoryImpl>(singletonCImpl, 44));
    }

    @Override
    public void injectMomoAgentApplication(MomoAgentApplication momoAgentApplication) {
      injectMomoAgentApplication2(momoAgentApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private MomoAgentApplication injectMomoAgentApplication2(MomoAgentApplication instance) {
      MomoAgentApplication_MembersInjector.injectDatabaseInitializer(instance, databaseInitializerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.momokotle.agent.data.local.DatabaseInitializer 
          return (T) new DatabaseInitializer();

          case 1: // com.momokotle.agent.data.datastore.UserAccountDataStore 
          return (T) new UserAccountDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.momokotle.agent.repository.FormSubmissionRepositoryImpl 
          return (T) new FormSubmissionRepositoryImpl(singletonCImpl.formSubmissionDao(), singletonCImpl.laravelApplicationRemoteDataSourceProvider.get(), singletonCImpl.networkMonitorProvider.get(), singletonCImpl.provideApplicationScopeProvider.get());

          case 3: // com.momokotle.agent.database.AppDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.momokotle.agent.data.remote.LaravelApplicationRemoteDataSource 
          return (T) new LaravelApplicationRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 5: // com.momokotle.agent.data.remote.MStorepayApiService 
          return (T) NetworkModule_ProvideMStorepayApiFactory.provideMStorepayApi(singletonCImpl.provideRetrofitProvider.get());

          case 6: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 7: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(singletonCImpl.authInterceptorProvider.get());

          case 8: // com.momokotle.agent.data.remote.AuthInterceptor 
          return (T) new AuthInterceptor(singletonCImpl.authTokenDataStoreProvider.get());

          case 9: // com.momokotle.agent.data.datastore.AuthTokenDataStore 
          return (T) new AuthTokenDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.momokotle.agent.utils.NetworkMonitor 
          return (T) new NetworkMonitor(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 11: // @com.momokotle.agent.di.ApplicationScope kotlinx.coroutines.CoroutineScope 
          return (T) AppModule_ProvideApplicationScopeFactory.provideApplicationScope();

          case 12: // com.momokotle.agent.repository.ProductRepositoryImpl 
          return (T) new ProductRepositoryImpl(singletonCImpl.storeProductDao(), singletonCImpl.laravelProductRemoteDataSourceProvider.get(), singletonCImpl.provideApplicationScopeProvider.get());

          case 13: // com.momokotle.agent.data.remote.LaravelProductRemoteDataSource 
          return (T) new LaravelProductRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 14: // com.momokotle.agent.repository.StoreRepositoryImpl 
          return (T) new StoreRepositoryImpl(singletonCImpl.orderRepositoryImplProvider.get());

          case 15: // com.momokotle.agent.repository.OrderRepositoryImpl 
          return (T) new OrderRepositoryImpl(singletonCImpl.storeOrderDao(), singletonCImpl.laravelOrderRemoteDataSourceProvider.get(), singletonCImpl.networkMonitorProvider.get(), singletonCImpl.provideApplicationScopeProvider.get());

          case 16: // com.momokotle.agent.data.remote.LaravelOrderRemoteDataSource 
          return (T) new LaravelOrderRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 17: // com.momokotle.agent.repository.AuthRepositoryImpl 
          return (T) new AuthRepositoryImpl(singletonCImpl.provideMStorepayApiProvider.get(), singletonCImpl.authTokenDataStoreProvider.get(), singletonCImpl.userAccountDataStoreProvider.get());

          case 18: // com.momokotle.agent.utils.FileDocumentHelper 
          return (T) new FileDocumentHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 19: // com.momokotle.agent.data.datastore.SettingsDataStore 
          return (T) new SettingsDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 20: // com.momokotle.agent.domain.repository.AccessibilityRepository 
          return (T) AppModule_ProvideAccessibilityRepositoryFactory.provideAccessibilityRepository(singletonCImpl.accessibilityRepositoryImplProvider.get());

          case 21: // com.momokotle.agent.repository.AccessibilityRepositoryImpl 
          return (T) new AccessibilityRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 22: // com.momokotle.agent.repository.ProfitRuleRepositoryImpl 
          return (T) new ProfitRuleRepositoryImpl(singletonCImpl.profitRuleDao());

          case 23: // com.momokotle.agent.repository.SavedContactRepositoryImpl 
          return (T) new SavedContactRepositoryImpl(singletonCImpl.savedContactDao());

          case 24: // com.momokotle.agent.ussd.UssdDialHelper 
          return (T) AppModule_ProvideUssdDialHelperFactory.provideUssdDialHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 25: // com.momokotle.agent.ussd.UssdAutomationEngine 
          return (T) AppModule_ProvideUssdAutomationEngineFactory.provideUssdAutomationEngine(singletonCImpl.automationLoggerProvider.get());

          case 26: // com.momokotle.agent.utils.AutomationLogger 
          return (T) new AutomationLogger();

          case 27: // com.momokotle.agent.repository.SystemUssdFlowRepositoryImpl 
          return (T) new SystemUssdFlowRepositoryImpl(singletonCImpl.provideMStorepayApiProvider.get(), singletonCImpl.systemUssdFlowDao(), singletonCImpl.ussdFlowCacheDataStoreProvider.get(), singletonCImpl.provideGsonProvider.get());

          case 28: // com.momokotle.agent.data.datastore.UssdFlowCacheDataStore 
          return (T) new UssdFlowCacheDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideGsonProvider.get());

          case 29: // com.google.gson.Gson 
          return (T) NetworkModule_ProvideGsonFactory.provideGson();

          case 30: // com.momokotle.agent.sms.CashOutSmsWatcher 
          return (T) new CashOutSmsWatcher(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.updateTransactionStatusUseCase(), singletonCImpl.automationLoggerProvider.get(), singletonCImpl.provideApplicationScopeProvider.get());

          case 31: // com.momokotle.agent.repository.TransactionRepositoryImpl 
          return (T) new TransactionRepositoryImpl(singletonCImpl.transactionDao());

          case 32: // com.momokotle.agent.domain.engine.AgentCommissionEngine 
          return (T) new AgentCommissionEngine();

          case 33: // com.momokotle.agent.domain.ussd.EntitlementClock 
          return (T) new EntitlementClock();

          case 34: // com.momokotle.agent.repository.CustomFlowRepositoryImpl 
          return (T) new CustomFlowRepositoryImpl(singletonCImpl.customFlowDao());

          case 35: // com.momokotle.agent.repository.BusinessSettingsRepositoryImpl 
          return (T) new BusinessSettingsRepositoryImpl(singletonCImpl.laravelBusinessSettingsRemoteDataSourceProvider.get());

          case 36: // com.momokotle.agent.data.remote.LaravelBusinessSettingsRemoteDataSource 
          return (T) new LaravelBusinessSettingsRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 37: // com.momokotle.agent.repository.SubscriptionRepositoryImpl 
          return (T) new SubscriptionRepositoryImpl(singletonCImpl.laravelSubscriptionPlanRemoteDataSourceProvider.get(), singletonCImpl.laravelSubscriptionRemoteDataSourceProvider.get());

          case 38: // com.momokotle.agent.data.remote.LaravelSubscriptionPlanRemoteDataSource 
          return (T) new LaravelSubscriptionPlanRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 39: // com.momokotle.agent.data.remote.LaravelSubscriptionRemoteDataSource 
          return (T) new LaravelSubscriptionRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 40: // com.momokotle.agent.repository.NotificationRepositoryImpl 
          return (T) new NotificationRepositoryImpl(singletonCImpl.laravelNotificationRemoteDataSourceProvider.get());

          case 41: // com.momokotle.agent.data.remote.LaravelNotificationRemoteDataSource 
          return (T) new LaravelNotificationRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 42: // com.momokotle.agent.repository.ReferralRepositoryImpl 
          return (T) new ReferralRepositoryImpl(singletonCImpl.laravelReferralRemoteDataSourceProvider.get());

          case 43: // com.momokotle.agent.data.remote.LaravelReferralRemoteDataSource 
          return (T) new LaravelReferralRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          case 44: // com.momokotle.agent.repository.WalletRepositoryImpl 
          return (T) new WalletRepositoryImpl(singletonCImpl.laravelWalletRemoteDataSourceProvider.get());

          case 45: // com.momokotle.agent.data.remote.LaravelWalletRemoteDataSource 
          return (T) new LaravelWalletRemoteDataSource(singletonCImpl.provideMStorepayApiProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
