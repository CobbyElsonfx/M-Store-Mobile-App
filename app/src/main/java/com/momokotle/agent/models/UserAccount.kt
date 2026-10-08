package com.momokotle.agent.models

data class UserAccount(
    val userId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String? = null,
    val location: String? = null,
    val accountStatus: String = UserProfile.ACCOUNT_STATUS_ACTIVE
)
