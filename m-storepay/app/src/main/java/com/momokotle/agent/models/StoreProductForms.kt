package com.momokotle.agent.models

/**
 * Store products that have a matching Services form. Those forms are opened
 * from Store and are not listed again under Services.
 */
object StoreProductForms {
    val linkedServiceForms: Set<AgentFormType> = setOf(
        AgentFormType.AGENT_CARD_REQUEST,
        AgentFormType.EVD_SIM_CONVERSION,
        AgentFormType.TELECEL_AGENT,
        AgentFormType.AIRTELTIGO_AGENT
    )

    fun formTypeFor(product: StoreProduct): AgentFormType? = formTypeForName(product.productName)

    fun formTypeForName(name: String): AgentFormType? {
        val n = name.trim()
        if (n.isEmpty()) return null
        return when {
            n.contains("EVD", ignoreCase = true) -> AgentFormType.EVD_SIM_CONVERSION
            n.contains("Telecel", ignoreCase = true) -> AgentFormType.TELECEL_AGENT
            n.contains("AirtelTigo", ignoreCase = true) ||
                n.contains("Airtel Tigo", ignoreCase = true) ||
                n.contains("Airtel-Tigo", ignoreCase = true) -> AgentFormType.AIRTELTIGO_AGENT
            n.contains("MTN", ignoreCase = true) -> AgentFormType.AGENT_CARD_REQUEST
            n.contains("Dynamic", ignoreCase = true) -> AgentFormType.DYNAMIC_SIM_BULK
            else -> null
        }
    }
}
