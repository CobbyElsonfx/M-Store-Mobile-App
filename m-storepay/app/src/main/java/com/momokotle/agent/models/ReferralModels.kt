package com.momokotle.agent.models

data class ReferralInfo(
    val referralCode: String,
    val referralLink: String,
    val stats: ReferralStats = ReferralStats(),
    val wallet: Wallet = Wallet(userId = "")
)

/** @deprecated Use [WalletTransactionRecord] for Firestore-backed wallet history. */
data class WalletTransaction(
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
