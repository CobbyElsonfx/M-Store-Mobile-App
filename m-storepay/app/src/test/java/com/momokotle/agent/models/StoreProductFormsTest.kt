package com.momokotle.agent.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreProductFormsTest {

    @Test
    fun mapsSeededSimProductsToTheirServiceForms() {
        assertEquals(AgentFormType.AGENT_CARD_REQUEST, StoreProductForms.formTypeForName("MTN Agent SIM"))
        assertEquals(AgentFormType.TELECEL_AGENT, StoreProductForms.formTypeForName("Telecel Agent SIM"))
        assertEquals(AgentFormType.AIRTELTIGO_AGENT, StoreProductForms.formTypeForName("AirtelTigo Agent SIM"))
        assertEquals(AgentFormType.EVD_SIM_CONVERSION, StoreProductForms.formTypeForName("EVD SIM Conversion"))
    }

    @Test
    fun storeLinkedFormsAreHiddenFromServices() {
        assertTrue(AgentFormType.AGENT_CARD_REQUEST in StoreProductForms.linkedServiceForms)
        assertTrue(AgentFormType.TELECEL_AGENT in StoreProductForms.linkedServiceForms)
        assertTrue(AgentFormType.AIRTELTIGO_AGENT in StoreProductForms.linkedServiceForms)
        assertTrue(AgentFormType.EVD_SIM_CONVERSION in StoreProductForms.linkedServiceForms)
        assertFalse(AgentFormType.DYNAMIC_SIM_BULK in StoreProductForms.linkedServiceForms)
        assertFalse(AgentFormType.PASSPORT in StoreProductForms.linkedServiceForms)
        assertFalse(AgentFormType.BUSINESS_CERTIFICATE in StoreProductForms.linkedServiceForms)
        assertFalse(AgentFormType.BIRTH_CERTIFICATE in StoreProductForms.linkedServiceForms)
    }

    @Test
    fun unknownOrBlankProductHasNoForm() {
        assertNull(StoreProductForms.formTypeForName("Agent Vest"))
        assertNull(StoreProductForms.formTypeForName(" "))
    }
}
