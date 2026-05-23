package com.momokotle.agent.models

data class UserAccount(
    val fullName: String,
    val email: String,
    val phoneNumber: String? = null
)
