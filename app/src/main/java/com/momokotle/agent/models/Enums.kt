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
    DEFAULT,
    CUSTOMIZE;

    val displayName: String
        get() = when (this) {
            DEFAULT -> "Default"
            CUSTOMIZE -> "Customize"
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

enum class AutomationSpeed {
    FAST,
    NORMAL,
    SLOW;

    val stepDelayMs: Long
        get() = when (this) {
            FAST -> 400L
            NORMAL -> 800L
            SLOW -> 1200L
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
    RUNNING,
    WAITING_FOR_USER,
    SUCCESS,
    FAILED,
    CANCELLED
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
