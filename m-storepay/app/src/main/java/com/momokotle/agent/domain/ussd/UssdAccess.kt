package com.momokotle.agent.domain.ussd

import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.UserBusinessProfile
import com.momokotle.agent.models.UserProfile

enum class UssdAccessResult {
    ALLOWED,
    SUBSCRIPTION_REQUIRED,
    SUBSCRIPTION_EXPIRED,
    SUBSCRIPTION_STATE_UNKNOWN;

    val userMessage: String
        get() = when (this) {
            ALLOWED -> ""
            SUBSCRIPTION_REQUIRED ->
                "An active subscription is required to use USSD automation."
            SUBSCRIPTION_EXPIRED ->
                "Your subscription has expired. Renew your subscription to continue using USSD automation."
            SUBSCRIPTION_STATE_UNKNOWN ->
                "Connect to the internet to verify your subscription before using USSD automation."
        }

    val actionLabel: String
        get() = when (this) {
            SUBSCRIPTION_EXPIRED -> "Renew Subscription"
            else -> "View Subscription Plans"
        }
}

class UssdSubscriptionRequiredException(
    val result: UssdAccessResult
) : IllegalStateException(result.userMessage)

object UssdAccessEvaluator {
    fun evaluate(profile: UserProfile?, nowMillis: Long): UssdAccessResult {
        if (profile == null || profile.lastServerSyncAt == null) {
            return UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN
        }
        return when (profile.toBusinessProfile().resolveAccessLevel(nowMillis)) {
            AccessLevel.FULL -> UssdAccessResult.ALLOWED
            AccessLevel.SUBSCRIPTION_REQUIRED -> UssdAccessResult.SUBSCRIPTION_EXPIRED
            AccessLevel.LIMITED -> UssdAccessResult.SUBSCRIPTION_REQUIRED
        }
    }
}

fun UserProfile.toBusinessProfile() = UserBusinessProfile(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    referralCode = referralCode,
    referredBy = referredBy,
    referredByUserId = referredByUserId,
    referralDate = referralDate,
    trialStartDate = trialStartDate,
    trialEndDate = trialEndDate,
    isTrialUsed = isTrialUsed,
    subscriptionStatus = subscriptionStatus,
    subscriptionExpiryDate = subscriptionExpiryDate,
    activeSubscriptionId = activeSubscriptionId,
    serverAccessLevel = serverAccessLevel
)
