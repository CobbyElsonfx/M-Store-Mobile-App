package com.momokotle.agent.ussd

/**
 * Decides whether an accessibility window is a USSD/dialer host we may type into.
 * MoMo Kotle’s own dashboard EditTexts must never be treated as USSD prompts.
 */
object UssdWindowGate {

    const val APP_PACKAGE = "com.momokotle.agent"

    /** Packages that host MTN USSD dialogs (mirrors accessibility_service_config). */
    private val USSD_HOST_PACKAGES = setOf(
        "com.android.phone",
        "com.google.android.dialer",
        "com.samsung.android.dialer",
        "com.android.server.telecom",
        "com.sec.android.app.popupuireceiver",
        "com.samsung.android.app.telephonyui",
        "com.android.incallui"
    )

    fun isOwnAppPackage(packageName: CharSequence?): Boolean {
        val pkg = packageName?.toString().orEmpty()
        return pkg == APP_PACKAGE || pkg.startsWith("$APP_PACKAGE.")
    }

    fun isUssdHostPackage(packageName: CharSequence?): Boolean {
        val pkg = packageName?.toString().orEmpty()
        if (pkg.isEmpty() || isOwnAppPackage(pkg)) return false
        if (pkg in USSD_HOST_PACKAGES) return true
        // Defensive: OEM dialer / telephony variants (never our app).
        return pkg.contains("dialer", ignoreCase = true) ||
            pkg.contains("telephony", ignoreCase = true) ||
            pkg.contains(".phone", ignoreCase = true) ||
            pkg.endsWith(".phone")
    }

    /** True when automation may type/click into this window. */
    fun mayAutomate(packageName: CharSequence?): Boolean = isUssdHostPackage(packageName)
}
