package com.momokotle.agent.domain.ussd

import android.os.SystemClock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntitlementClock @Inject constructor() {
    fun wallNow(): Long = System.currentTimeMillis()

    fun elapsedNow(): Long = SystemClock.elapsedRealtime()

    /**
     * Advances from the last Laravel sync using elapsed realtime so setting the
     * phone clock backwards cannot keep an expired subscription usable.
     */
    fun estimatedNow(lastServerSyncAt: Long?, elapsedRealtimeAtSync: Long?): Long =
        estimate(wallNow(), elapsedNow(), lastServerSyncAt, elapsedRealtimeAtSync)

    companion object {
        fun estimate(
            wallNow: Long,
            elapsedNow: Long,
            lastServerSyncAt: Long?,
            elapsedRealtimeAtSync: Long?
        ): Long {
            val lastSync = lastServerSyncAt ?: return wallNow
            val elapsedAtSync = elapsedRealtimeAtSync ?: return wallNow
            if (elapsedNow < elapsedAtSync) {
                return maxOf(wallNow, lastSync)
            }
            return lastSync + (elapsedNow - elapsedAtSync)
        }
    }
}
