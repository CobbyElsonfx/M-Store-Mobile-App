package com.momokotle.agent.models

data class ReferralInfo(
    val referralCode: String,
    val referralLink: String,
    val totalReferrals: Int = 0,
    val pendingEarnings: Double = 0.0,
    val totalEarnings: Double = 0.0
)

data class WalletTransaction(
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
