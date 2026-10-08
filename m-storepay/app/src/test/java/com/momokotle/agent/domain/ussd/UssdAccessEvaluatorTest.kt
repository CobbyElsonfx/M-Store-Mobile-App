package com.momokotle.agent.domain.ussd

import com.momokotle.agent.models.SubscriptionStatus
import com.momokotle.agent.models.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class UssdAccessEvaluatorTest {

    private val now = 1_700_000_000_000L

    @Test
    fun activeSubscription_allowsUssd() {
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(synced(SubscriptionStatus.ACTIVE, expiry = now + 60_000), now)
        )
    }

    @Test
    fun activeSubscriptionOffline_stillAllowedBeforeExpiry() {
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(synced(SubscriptionStatus.ACTIVE, expiry = now + 1), now)
        )
    }

    @Test
    fun activeStatusPastExpiry_isBlocked() {
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_EXPIRED,
            UssdAccessEvaluator.evaluate(synced(SubscriptionStatus.ACTIVE, expiry = now), now)
        )
    }

    @Test
    fun expiredStatus_isBlocked() {
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_EXPIRED,
            UssdAccessEvaluator.evaluate(synced(SubscriptionStatus.EXPIRED, expiry = now - 1), now)
        )
    }

    @Test
    fun neverSubscribed_isBlocked() {
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_REQUIRED,
            UssdAccessEvaluator.evaluate(synced(SubscriptionStatus.NONE), now)
        )
    }

    @Test
    fun missingProfile_isUnknown() {
        assertEquals(UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN, UssdAccessEvaluator.evaluate(null, now))
    }

    @Test
    fun unsyncedProfile_isUnknown() {
        val profile = UserProfile(
            userId = "u1",
            fullName = "Agent",
            email = "a@test.com",
            phoneNumber = "0241234567",
            location = "",
            subscriptionStatus = SubscriptionStatus.ACTIVE,
            subscriptionExpiryDate = now + 60_000
        )
        assertEquals(UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN, UssdAccessEvaluator.evaluate(profile, now))
    }

    @Test
    fun activeTrial_allowsUssd() {
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.TRIAL, trialEnd = now + 60_000),
                now
            )
        )
    }

    @Test
    fun expiredTrial_isBlocked() {
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_EXPIRED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.TRIAL, trialEnd = now - 1),
                now
            )
        )
    }

    @Test
    fun subscriptionBecomesAllowedAfterActivation() {
        val before = synced(SubscriptionStatus.NONE)
        val after = synced(SubscriptionStatus.ACTIVE, expiry = now + 86_400_000L)
        assertEquals(UssdAccessResult.SUBSCRIPTION_REQUIRED, UssdAccessEvaluator.evaluate(before, now))
        assertEquals(UssdAccessResult.ALLOWED, UssdAccessEvaluator.evaluate(after, now))
    }

    @Test
    fun backendFullAccessUnlocksStaleExpiredCacheWithoutPastExpiryDate() {
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.NONE).copy(
                    serverAccessLevel = com.momokotle.agent.models.AccessLevel.FULL
                ),
                now
            )
        )
    }

    @Test
    fun backendExpiredOverridesStaleLocalActiveWhenExpiryHasPassed() {
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_EXPIRED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.ACTIVE, expiry = now - 1).copy(
                    serverAccessLevel = com.momokotle.agent.models.AccessLevel.FULL
                ),
                now
            )
        )
    }

    @Test
    fun activeWithoutParsedExpiryStillAllowedWhenBackendSaysFull() {
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.ACTIVE, expiry = null).copy(
                    serverAccessLevel = com.momokotle.agent.models.AccessLevel.FULL
                ),
                now
            )
        )
    }

    @Test
    fun clockRollbackDoesNotKeepExpiredAccess() {
        val lastSync = now
        val expiry = now + 10_000
        val elapsedAtSync = 5_000L
        val estimated = EntitlementClock.estimate(
            wallNow = now - 86_400_000L,
            elapsedNow = elapsedAtSync + 20_000L,
            lastServerSyncAt = lastSync,
            elapsedRealtimeAtSync = elapsedAtSync
        )
        assertEquals(
            UssdAccessResult.SUBSCRIPTION_EXPIRED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.ACTIVE, expiry = expiry, lastSync = lastSync),
                estimated
            )
        )
    }

    @Test
    fun elapsedTimeBeforeExpiry_staysAllowed() {
        val lastSync = now
        val estimated = EntitlementClock.estimate(
            wallNow = now,
            elapsedNow = 8_000L,
            lastServerSyncAt = lastSync,
            elapsedRealtimeAtSync = 5_000L
        )
        assertEquals(
            UssdAccessResult.ALLOWED,
            UssdAccessEvaluator.evaluate(
                synced(SubscriptionStatus.ACTIVE, expiry = now + 10_000, lastSync = lastSync),
                estimated
            )
        )
    }

    @Test
    fun messages_matchProductCopy() {
        assertEquals(
            "An active subscription is required to use USSD automation.",
            UssdAccessResult.SUBSCRIPTION_REQUIRED.userMessage
        )
        assertEquals(
            "Your subscription has expired. Renew your subscription to continue using USSD automation.",
            UssdAccessResult.SUBSCRIPTION_EXPIRED.userMessage
        )
        assertEquals(
            "Connect to the internet to verify your subscription before using USSD automation.",
            UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN.userMessage
        )
        assertNotEquals(UssdAccessResult.ALLOWED.userMessage, UssdAccessResult.SUBSCRIPTION_REQUIRED.userMessage)
    }

    private fun synced(
        status: SubscriptionStatus,
        expiry: Long? = null,
        trialEnd: Long? = null,
        lastSync: Long = now
    ) = UserProfile(
        userId = "u1",
        fullName = "Agent",
        email = "a@test.com",
        phoneNumber = "0241234567",
        location = "",
        subscriptionStatus = status,
        subscriptionExpiryDate = expiry,
        trialEndDate = trialEnd,
        lastServerSyncAt = lastSync,
        elapsedRealtimeAtSync = 1_000L
    )
}
