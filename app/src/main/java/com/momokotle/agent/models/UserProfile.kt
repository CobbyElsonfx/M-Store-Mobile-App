package com.momokotle.agent.models

data class UserProfile(
    val userId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val accountStatus: String = ACCOUNT_STATUS_ACTIVE
) {
    val isActive: Boolean
        get() = accountStatus.equals(ACCOUNT_STATUS_ACTIVE, ignoreCase = true)

    val isSuspended: Boolean
        get() = accountStatus.equals(ACCOUNT_STATUS_SUSPENDED, ignoreCase = true)

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
