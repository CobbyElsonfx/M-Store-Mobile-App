package com.momokotle.agent.models

enum class TransactionType {
    MTN_CASHIN,
    MTN_CASHOUT,
    AGENT_MERCHANT,
    MTN_AIRTIME,
    BALANCE,
    COMMISSION,
    EVD_JUST4U,
    CUSTOM
}

enum class TransactionStatus {
    ATTEMPTED,
    PENDING,
    SUCCESSFUL,
    FAILED
}

enum class SimSlot {
    SIM1,
    SIM2,
    NONE
}

enum class SimPurpose {
    MTN_MOBILE_MONEY,
    TELECEL_CASH,
    AIRTELTIGO_CASH,
    MTN_AIRTIME_TRANSFER
}

enum class ProfitCalculationMode {
    PERCENTAGE,
    FIXED,
    TIERED;

    val displayName: String
        get() = when (this) {
            PERCENTAGE -> "Percentage"
            FIXED -> "Fixed"
            TIERED -> "Tiered"
        };

    companion object {
        fun fromStored(value: String?): ProfitCalculationMode = when (value?.trim()) {
            null, "DEFAULT", "PERCENTAGE" -> PERCENTAGE
            "CUSTOMIZE", "CUSTOM", "TIERED" -> TIERED
            "FIXED" -> FIXED
            else -> runCatching { valueOf(value!!) }.getOrDefault(PERCENTAGE)
        }
    }
}

enum class ProfitRuleType {
    CASH_IN,
    CASH_OUT;

    val displayName: String
        get() = when (this) {
            CASH_IN -> "Cash In"
            CASH_OUT -> "Cash Out"
        }
}

/**
 * Local duplicate-phone warning window (DataStore). Off skips the check.
 * Default: [FIVE_MINUTES].
 */
enum class DuplicateTransactionWarningWindow {
    OFF,
    ONE_MINUTE,
    FIVE_MINUTES,
    TEN_MINUTES,
    FIFTEEN_MINUTES,
    THIRTY_MINUTES;

    val displayName: String
        get() = when (this) {
            OFF -> "Off"
            ONE_MINUTE -> "1 minute"
            FIVE_MINUTES -> "5 minutes"
            TEN_MINUTES -> "10 minutes"
            FIFTEEN_MINUTES -> "15 minutes"
            THIRTY_MINUTES -> "30 minutes"
        }

    /** Null means the warning is disabled. */
    val windowMs: Long?
        get() = when (this) {
            OFF -> null
            ONE_MINUTE -> 60_000L
            FIVE_MINUTES -> 5 * 60_000L
            TEN_MINUTES -> 10 * 60_000L
            FIFTEEN_MINUTES -> 15 * 60_000L
            THIRTY_MINUTES -> 30 * 60_000L
        }

    companion object {
        fun fromStored(value: String?): DuplicateTransactionWarningWindow = when (value?.trim()) {
            "OFF" -> OFF
            "ONE_MINUTE" -> ONE_MINUTE
            "TEN_MINUTES" -> TEN_MINUTES
            "FIFTEEN_MINUTES" -> FIFTEEN_MINUTES
            "THIRTY_MINUTES" -> THIRTY_MINUTES
            "FIVE_MINUTES" -> FIVE_MINUTES
            else -> FIVE_MINUTES
        }
    }
}

enum class AutomationSpeed {
    FAST,
    NORMAL,
    SAFE;

    val displayName: String
        get() = when (this) {
            FAST -> "Fast"
            NORMAL -> "Normal"
            SAFE -> "Safe"
        }

    val description: String
        get() = when (this) {
            FAST -> "Extra fast steps; best on responsive devices"
            NORMAL -> "Balanced for most devices"
            SAFE -> "Slower; more reliable on older phones"
        }

    /** Debounce window changes before acting. */
    val debounceMs: Long
        get() = when (this) {
            FAST -> 35L
            NORMAL -> 150L
            SAFE -> 280L
        }

    /** Wait after typing before tapping OK/Send. */
    val submitSettleMs: Long
        get() = when (this) {
            FAST -> 50L
            NORMAL -> 250L
            SAFE -> 450L
        }

    /** Primary delay stored on the automation engine. */
    val stepDelayMs: Long
        get() = submitSettleMs

    companion object {
        fun fromStored(value: String?): AutomationSpeed = when (value?.trim()) {
            "FAST" -> FAST
            "SAFE", "SLOW" -> SAFE // SLOW was the previous name
            "NORMAL" -> NORMAL
            else -> NORMAL
        }
    }
}

enum class ContactNetwork {
    MTN,
    TELECEL,
    AIRTELTIGO,
    UNKNOWN;

    val displayName: String
        get() = when (this) {
            MTN -> "MTN"
            TELECEL -> "Telecel"
            AIRTELTIGO -> "AirtelTigo"
            UNKNOWN -> "Unknown"
        }
}

enum class AutomationStatus {
    IDLE,
    STARTING,
    DIALING,
    RUNNING,
    WAITING_FOR_PIN,
    SUCCESS,
    FAILED,
    CANCELLED,
    TIMEOUT,
    /** USSD dialog/session closed; automation released without a financial success claim. */
    SESSION_ENDED;

    val isActive: Boolean
        get() = this == STARTING || this == DIALING || this == RUNNING || this == WAITING_FOR_PIN

    val isTerminal: Boolean
        get() = this == SUCCESS || this == FAILED || this == CANCELLED ||
            this == TIMEOUT || this == SESSION_ENDED
}

enum class UssdActionType {
    CLICK,
    INPUT,
    WAIT,
    STOP_FOR_USER
}

fun simPurposeForTransaction(type: TransactionType): SimPurpose = when (type) {
    TransactionType.MTN_AIRTIME -> SimPurpose.MTN_AIRTIME_TRANSFER
    else -> SimPurpose.MTN_MOBILE_MONEY
}
