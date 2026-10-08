package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authTokenStore: DataStore<Preferences> by preferencesDataStore(name = "auth_token")

@Singleton
class AuthTokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    @Volatile
    private var cachedToken: String? = null

    init {
        cachedToken = runBlocking { readTokenFromStore() }
    }

    fun observeToken(): Flow<String?> = context.authTokenStore.data.map { prefs ->
        prefs[Keys.ACCESS_TOKEN]?.also { cachedToken = it }
    }

    fun currentToken(): String? = cachedToken

    suspend fun saveToken(token: String) {
        cachedToken = token
        context.authTokenStore.edit { it[Keys.ACCESS_TOKEN] = token }
    }

    suspend fun clearToken() {
        cachedToken = null
        context.authTokenStore.edit { it.remove(Keys.ACCESS_TOKEN) }
    }

    suspend fun getToken(): String? {
        val token = readTokenFromStore()
        cachedToken = token
        return token
    }

    private suspend fun readTokenFromStore(): String? =
        context.authTokenStore.data.first()[Keys.ACCESS_TOKEN]
}
