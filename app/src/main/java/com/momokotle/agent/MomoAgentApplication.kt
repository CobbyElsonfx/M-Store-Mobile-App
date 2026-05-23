package com.momokotle.agent

import android.app.Application
import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.data.local.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MomoAgentApplication : Application() {

    @Inject lateinit var databaseInitializer: DatabaseInitializer
    @Inject lateinit var userAccountDataStore: UserAccountDataStore

    override fun onCreate() {
        super.onCreate()
        databaseInitializer.seedDefaults()
        CoroutineScope(Dispatchers.IO).launch {
            userAccountDataStore.seedDefaultIfEmpty()
            userAccountDataStore.getUserId()
        }
    }
}
