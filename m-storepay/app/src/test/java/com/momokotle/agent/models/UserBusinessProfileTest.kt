package com.momokotle.agent.models

import org.junit.Assert.assertEquals
import org.junit.Test

class UserBusinessProfileTest {

    @Test
    fun trialActive_grantsFullAccess() {
        val now = System.currentTimeMillis()
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.TRIAL,
            trialEndDate = now + 86_400_000L
        )
        assertEquals(AccessLevel.FULL, profile.resolveAccessLevel(now))
    }

    @Test
    fun expiredTrial_requiresSubscription() {
        val now = System.currentTimeMillis()
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.TRIAL,
            trialEndDate = now - 1_000L,
            isTrialUsed = true
        )
        assertEquals(AccessLevel.SUBSCRIPTION_REQUIRED, profile.resolveAccessLevel(now))
    }

    @Test
    fun activeSubscription_grantsFullAccess() {
        val now = System.currentTimeMillis()
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.ACTIVE,
            subscriptionExpiryDate = now + 86_400_000L
        )
        assertEquals(AccessLevel.FULL, profile.resolveAccessLevel(now))
    }

    @Test
    fun activeSubscriptionWithoutParsedExpiry_trustsActiveStatus() {
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.ACTIVE,
            subscriptionExpiryDate = null,
            serverAccessLevel = AccessLevel.FULL
        )
        assertEquals(AccessLevel.FULL, profile.resolveAccessLevel())
    }

    @Test
    fun backendFullAccessOverridesStaleNoneUntilDatesProveExpiry() {
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.NONE,
            serverAccessLevel = AccessLevel.FULL
        )
        assertEquals(AccessLevel.FULL, profile.resolveAccessLevel())
    }

    @Test
    fun neverPaid_hasLimitedAccess() {
        val profile = UserBusinessProfile(
            userId = "u1",
            subscriptionStatus = SubscriptionStatus.NONE
        )
        assertEquals(AccessLevel.LIMITED, profile.resolveAccessLevel())
    }
}
