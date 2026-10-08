package com.momokotle.agent.models

data class UserProfile(
    val userId: String,
    val firstName: String = "",
    val lastName: String = "",
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val accountStatus: String = ACCOUNT_STATUS_ACTIVE,
    val referralCode: String = "",
    val referredBy: String? = null,
    val referredByUserId: String? = null,
    val referralDate: Long? = null,
    val trialStartDate: Long? = null,
    val trialEndDate: Long? = null,
    val isTrialUsed: Boolean = false,
    val subscriptionStatus: SubscriptionStatus = SubscriptionStatus.NONE,
    val subscriptionExpiryDate: Long? = null,
    val activeSubscriptionId: String? = null,
    val lastServerSyncAt: Long? = null,
    val elapsedRealtimeAtSync: Long? = null,
    val serverAccessLevel: AccessLevel? = null
) {
    val isActive: Boolean
        get() = accountStatus.equals(ACCOUNT_STATUS_ACTIVE, ignoreCase = true)

    val isSuspended: Boolean
        get() = accountStatus.equals(ACCOUNT_STATUS_SUSPENDED, ignoreCase = true)

    val accessLevel: AccessLevel
        get() = UserBusinessProfile(
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
        ).resolveAccessLevel()

    val hasFullAccess: Boolean
        get() = accessLevel == AccessLevel.FULL

    fun toUserAccount() = UserAccount(
        userId = userId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        location = location,
        accountStatus = accountStatus
    )

    companion object {
        const val ACCOUNT_STATUS_ACTIVE = "Active"
        const val ACCOUNT_STATUS_SUSPENDED = "Suspended"
    }
}
