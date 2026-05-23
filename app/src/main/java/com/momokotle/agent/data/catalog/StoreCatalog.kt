package com.momokotle.agent.data.catalog

import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreItem

object StoreCatalog {
    val items: List<StoreItem> = listOf(
        StoreItem(
            id = "sim_mtn",
            itemName = "MTN SIM Card",
            itemDescription = "New MTN SIM for agent operations",
            price = 5.0,
            category = StoreCategory.SIM_CARDS
        ),
        StoreItem(
            id = "sim_telecel",
            itemName = "Telecel SIM Card",
            itemDescription = "Telecel SIM for agent registration",
            price = 5.0,
            category = StoreCategory.SIM_CARDS
        ),
        StoreItem(
            id = "sim_airteltigo",
            itemName = "AirtelTigo SIM Card",
            itemDescription = "AirtelTigo SIM for agent use",
            price = 5.0,
            category = StoreCategory.SIM_CARDS
        ),
        StoreItem(
            id = "mat_agent_card",
            itemName = "Agent Card Holder",
            itemDescription = "Protective holder for MoMo agent card",
            price = 15.0,
            category = StoreCategory.AGENT_MATERIALS
        ),
        StoreItem(
            id = "mat_receipt_book",
            itemName = "Receipt Book",
            itemDescription = "Agent receipt book for customer records",
            price = 25.0,
            category = StoreCategory.AGENT_MATERIALS
        ),
        StoreItem(
            id = "mat_sticker_pack",
            itemName = "Agent Branding Stickers",
            itemDescription = "Sticker pack for agent point branding",
            price = 10.0,
            category = StoreCategory.AGENT_MATERIALS
        )
    )

    fun byCategory(category: StoreCategory): List<StoreItem> =
        items.filter { it.category == category }
}
