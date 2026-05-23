package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.momokotle.agent.models.UserAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userAccountStore: DataStore<Preferences> by preferencesDataStore(name = "user_account")

@Singleton
class UserAccountDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val FULL_NAME = stringPreferencesKey("full_name")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone")
    }

    val account: Flow<UserAccount?> = context.userAccountStore.data.map { prefs ->
        val name = prefs[Keys.FULL_NAME]
        val email = prefs[Keys.EMAIL]
        if (name.isNullOrBlank() || email.isNullOrBlank()) {
            null
        } else {
            UserAccount(
                fullName = name,
                email = email,
                phoneNumber = prefs[Keys.PHONE]?.takeIf { it.isNotBlank() }
            )
        }
    }

    /** Called once during account creation / login — not exposed in Settings UI. */
    suspend fun saveAccount(account: UserAccount) {
        context.userAccountStore.edit {
            it[Keys.FULL_NAME] = account.fullName
            it[Keys.EMAIL] = account.email
            if (account.phoneNumber.isNullOrBlank()) {
                it.remove(Keys.PHONE)
            } else {
                it[Keys.PHONE] = account.phoneNumber
            }
        }
    }

    suspend fun getUserId(): String {
        val prefs = context.userAccountStore.data.first()
        val existing = prefs[Keys.USER_ID]
        if (!existing.isNullOrBlank()) return existing
        val newId = UUID.randomUUID().toString()
        context.userAccountStore.edit { it[Keys.USER_ID] = newId }
        return newId
    }

    suspend fun seedDefaultIfEmpty() {
        val current = account.first()
        if (current != null) return
        saveAccount(
            UserAccount(
                fullName = "Agent User",
                email = "agent@mstorepay.com",
                phoneNumber = "0240000000"
            )
        )
    }
}
