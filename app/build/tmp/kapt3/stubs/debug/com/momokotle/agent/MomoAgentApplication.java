package com.momokotle.agent;

import android.app.Application;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.data.local.DatabaseInitializer;
import dagger.hilt.android.HiltAndroidApp;
import kotlinx.coroutines.Dispatchers;
import javax.inject.Inject;

@dagger.hilt.android.HiltAndroidApp()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0011"}, d2 = {"Lcom/momokotle/agent/MomoAgentApplication;", "Landroid/app/Application;", "()V", "databaseInitializer", "Lcom/momokotle/agent/data/local/DatabaseInitializer;", "getDatabaseInitializer", "()Lcom/momokotle/agent/data/local/DatabaseInitializer;", "setDatabaseInitializer", "(Lcom/momokotle/agent/data/local/DatabaseInitializer;)V", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "getUserAccountDataStore", "()Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "setUserAccountDataStore", "(Lcom/momokotle/agent/data/datastore/UserAccountDataStore;)V", "onCreate", "", "app_debug"})
public final class MomoAgentApplication extends android.app.Application {
    @javax.inject.Inject()
    public com.momokotle.agent.data.local.DatabaseInitializer databaseInitializer;
    @javax.inject.Inject()
    public com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore;
    
    public MomoAgentApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.data.local.DatabaseInitializer getDatabaseInitializer() {
        return null;
    }
    
    public final void setDatabaseInitializer(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.local.DatabaseInitializer p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.data.datastore.UserAccountDataStore getUserAccountDataStore() {
        return null;
    }
    
    public final void setUserAccountDataStore(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}