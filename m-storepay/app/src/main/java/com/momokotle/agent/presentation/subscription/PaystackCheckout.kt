package com.momokotle.agent.presentation.subscription

import com.paystack.android.core.Paystack

object PaystackCheckout {
    @Volatile
    private var initializedKey: String? = null

    fun ensurePublicKey(publicKey: String) {
        if (publicKey.isBlank() || initializedKey == publicKey) {
            return
        }
        synchronized(this) {
            if (initializedKey == publicKey) return
            try {
                Paystack.builder()
                    .setPublicKey(publicKey)
                    .build()
            } catch (_: IllegalStateException) {
                // SDK already initialized for this process.
            }
            initializedKey = publicKey
        }
    }
}
