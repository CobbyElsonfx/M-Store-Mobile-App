package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.momokotle.agent.models.UserAccount
import com.momokotle.agent.models.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
        val LOCATION = stringPreferencesKey("location")
        val ACCOUNT_STATUS = stringPreferencesKey("account_status")
    }

    fun observeProfile(): Flow<UserProfile?> = context.userAccountStore.data.map { prefs ->
        val userId = prefs[Keys.USER_ID]
        val fullName = prefs[Keys.FULL_NAME]
        val email = prefs[Keys.EMAIL]
        if (userId.isNullOrBlank() || fullName.isNullOrBlank() || email.isNullOrBlank()) {
            null
        } else {
            UserProfile(
                userId = userId,
                fullName = fullName,
                email = email,
                phoneNumber = prefs[Keys.PHONE].orEmpty(),
                location = prefs[Keys.LOCATION].orEmpty(),
                accountStatus = prefs[Keys.ACCOUNT_STATUS] ?: UserProfile.ACCOUNT_STATUS_ACTIVE
            )
        }
    }

    val account: Flow<UserAccount?> = observeProfile().map { profile ->
        profile?.toUserAccount()
    }

    suspend fun getProfile(): UserProfile? = observeProfile().first()

    suspend fun saveProfile(profile: UserProfile) {
        context.userAccountStore.edit {
            it[Keys.USER_ID] = profile.userId
            it[Keys.FULL_NAME] = profile.fullName
            it[Keys.EMAIL] = profile.email
            it[Keys.PHONE] = profile.phoneNumber
            it[Keys.LOCATION] = profile.location
            it[Keys.ACCOUNT_STATUS] = profile.accountStatus
        }
    }

    suspend fun clearProfile() {
        context.userAccountStore.edit { it.clear() }
    }

    /** @deprecated Use [AuthRepository.getCurrentUserId] instead. */
    suspend fun getUserId(): String {
        val profile = getProfile()
        if (!profile?.userId.isNullOrBlank()) return profile!!.userId
        throw IllegalStateException("No authenticated user")
    }
}
