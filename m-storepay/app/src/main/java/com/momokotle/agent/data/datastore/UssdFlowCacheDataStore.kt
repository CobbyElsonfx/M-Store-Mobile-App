package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.momokotle.agent.data.remote.ApiUssdFlowDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.ussdFlowStore: DataStore<Preferences> by preferencesDataStore(name = "ussd_flow_cache")

@Singleton
class UssdFlowCacheDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private object Keys {
        val FLOWS_JSON = stringPreferencesKey("flows_json")
    }

    suspend fun saveFlows(flows: List<ApiUssdFlowDto>) {
        context.ussdFlowStore.edit {
            it[Keys.FLOWS_JSON] = gson.toJson(flows)
        }
    }

    suspend fun getFlows(): List<ApiUssdFlowDto>? {
        val json = context.ussdFlowStore.data.first()[Keys.FLOWS_JSON] ?: return null
        return runCatching {
            gson.fromJson<List<ApiUssdFlowDto>>(
                json,
                object : TypeToken<List<ApiUssdFlowDto>>() {}.type
            )
        }.getOrNull()
    }

    suspend fun clear() {
        context.ussdFlowStore.edit { it.remove(Keys.FLOWS_JSON) }
    }
}
