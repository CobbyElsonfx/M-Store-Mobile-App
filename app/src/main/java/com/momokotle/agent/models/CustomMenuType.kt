package com.momokotle.agent.models

enum class CustomMenuType {
    /** Step-based USSD automation from the base code. */
    STANDARD,
    /** Dial a resolved USSD string (placeholders in code replaced, no menu steps). */
    DIRECT
}
