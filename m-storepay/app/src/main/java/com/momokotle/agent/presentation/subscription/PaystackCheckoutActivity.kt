package com.momokotle.agent.presentation.subscription

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.paystack.android.ui.paymentsheet.PaymentSheet
import com.paystack.android.ui.paymentsheet.PaymentSheetResult

class PaystackCheckoutActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var accessCode: String
    private var launched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launched = savedInstanceState?.getBoolean(STATE_LAUNCHED) == true
        val publicKey = intent.getStringExtra(EXTRA_PUBLIC_KEY).orEmpty()
        accessCode = intent.getStringExtra(EXTRA_ACCESS_CODE).orEmpty()
        if (publicKey.isBlank() || accessCode.isBlank()) {
            finishWith(RESULT_FAILED, "Paystack checkout was not initialized.")
            return
        }

        val spinner = ProgressBar(this).apply { isIndeterminate = true }
        setContentView(FrameLayout(this).apply {
            addView(
                spinner,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        })

        try {
            PaystackCheckout.ensurePublicKey(publicKey)
            paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        } catch (error: Exception) {
            finishWith(RESULT_FAILED, error.message ?: "Could not open Paystack checkout.")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LAUNCHED, launched)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (!launched && ::paymentSheet.isInitialized) {
            launched = true
            try {
                paymentSheet.launch(accessCode)
            } catch (error: Exception) {
                finishWith(RESULT_FAILED, error.message ?: "Could not launch Paystack checkout.")
            }
        }
    }

    private fun onPaymentSheetResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Completed -> {
                val reference = result.paymentCompletionDetails.reference
                finishWith(Activity.RESULT_OK, reference = reference)
            }
            PaymentSheetResult.Cancelled -> finishWith(Activity.RESULT_CANCELED)
            is PaymentSheetResult.Failed -> finishWith(
                RESULT_FAILED,
                result.error.message ?: "Payment failed"
            )
        }
    }

    private fun finishWith(
        resultCode: Int,
        errorMessage: String? = null,
        reference: String? = null
    ) {
        setResult(
            resultCode,
            Intent().apply {
                if (!errorMessage.isNullOrBlank()) {
                    putExtra(EXTRA_ERROR, errorMessage)
                }
                if (!reference.isNullOrBlank()) {
                    putExtra(EXTRA_REFERENCE, reference)
                }
            }
        )
        finish()
    }

    companion object {
        private const val STATE_LAUNCHED = "launched"
        const val EXTRA_PUBLIC_KEY = "public_key"
        const val EXTRA_ACCESS_CODE = "access_code"
        const val EXTRA_ERROR = "error"
        const val EXTRA_REFERENCE = "reference"
        const val RESULT_FAILED = Activity.RESULT_FIRST_USER

        fun intent(context: Context, publicKey: String, accessCode: String): Intent =
            Intent(context, PaystackCheckoutActivity::class.java)
                .putExtra(EXTRA_PUBLIC_KEY, publicKey)
                .putExtra(EXTRA_ACCESS_CODE, accessCode)
    }
}
