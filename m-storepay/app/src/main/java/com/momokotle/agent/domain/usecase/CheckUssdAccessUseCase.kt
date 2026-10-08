package com.momokotle.agent.domain.usecase

import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.domain.ussd.EntitlementClock
import com.momokotle.agent.domain.ussd.UssdAccessEvaluator
import com.momokotle.agent.domain.ussd.UssdAccessResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckUssdAccessUseCase @Inject constructor(
    private val userAccountDataStore: UserAccountDataStore,
    private val clock: EntitlementClock
) {
    suspend operator fun invoke(): UssdAccessResult {
        val profile = userAccountDataStore.getProfile()
        val now = clock.estimatedNow(profile?.lastServerSyncAt, profile?.elapsedRealtimeAtSync)
        return UssdAccessEvaluator.evaluate(profile, now)
    }

    fun observe(): Flow<UssdAccessResult> =
        userAccountDataStore.observeProfile().map { profile ->
            UssdAccessEvaluator.evaluate(
                profile,
                clock.estimatedNow(profile?.lastServerSyncAt, profile?.elapsedRealtimeAtSync)
            )
        }
}
