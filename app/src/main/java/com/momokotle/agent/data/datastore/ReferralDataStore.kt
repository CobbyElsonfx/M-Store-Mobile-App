package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.models.ReferralInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.referralStore: DataStore<Preferences> by preferencesDataStore(name = "referral")

@Singleton
class ReferralDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) {
    private object Keys {
        val REFERRAL_CODE     = stringPreferencesKey("referral_code")
        val TOTAL_EARNINGS    = doublePreferencesKey("total_earnings")
        val PENDING_EARNINGS  = doublePreferencesKey("pending_earnings")
    }

    private val codeFlow: Flow<String> = context.referralStore.data.map { prefs ->
        prefs[Keys.REFERRAL_CODE].orEmpty()
    }

    val referralInfo: Flow<ReferralInfo> = combine(
        codeFlow,
        context.referralStore.data.map { it[Keys.TOTAL_EARNINGS] ?: 0.0 },
        context.referralStore.data.map { it[Keys.PENDING_EARNINGS] ?: 0.0 }
    ) { code, total, pending ->
        ReferralInfo(
            referralCode = code,
            referralLink = "https://mstorepay.com/join?ref=$code",
            totalEarnings = total,
            pendingEarnings = pending
        )
    }

    /** Generates and persists a referral code on first call, then returns it as-is. */
    suspend fun ensureReferralCode() {
        val prefs = context.referralStore.data.first()
        if (!prefs[Keys.REFERRAL_CODE].isNullOrBlank()) return
        val userId = authRepository.getCurrentUserId() ?: return
        val code = "REF" + userId.take(8).uppercase()
        context.referralStore.edit { it[Keys.REFERRAL_CODE] = code }
    }
}
