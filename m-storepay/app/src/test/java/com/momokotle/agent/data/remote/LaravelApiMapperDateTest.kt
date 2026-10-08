package com.momokotle.agent.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LaravelApiMapperDateTest {

    @Test
    fun parseIsoMillis_acceptsUtcZ() {
        val millis = LaravelApiMapper.parseIsoMillis("2026-10-18T12:00:00Z")
        assertNotNull(millis)
        assertEquals(LaravelApiMapper.parseIsoMillis("2026-10-18T12:00:00+00:00"), millis)
    }

    @Test
    fun parseIsoMillis_acceptsOffset() {
        val millis = LaravelApiMapper.parseIsoMillis("2026-10-18T12:00:00+00:00")
        assertNotNull(millis)
    }

    @Test
    fun mapUser_activeStatusGrantsFullAccessWhenExpiryParses() {
        val profile = LaravelApiMapper.mapUser(
            ApiUserDto(
                id = 9,
                email = "agent@test.com",
                subscriptionStatus = "Active",
                subscriptionExpiryDate = "2026-12-31T12:00:00Z",
                accessLevel = "full",
                isActive = true
            )
        )
        assertEquals(com.momokotle.agent.models.SubscriptionStatus.ACTIVE, profile.subscriptionStatus)
        assertEquals(com.momokotle.agent.models.AccessLevel.FULL, profile.accessLevel)
        assertEquals(true, profile.hasFullAccess)
    }

    @Test
    fun mapBusinessSettings_includesManualPaymentDetails() {
        val settings = LaravelApiMapper.mapBusinessSettings(
            ApiBusinessSettingsDto(
                currency = "GHS",
                paystackPaymentEnabled = false,
                manualPaymentEnabled = false,
                supportWhatsAppNumber = "0244123456",
                manualPayment = ApiManualPaymentDetailsDto(
                    accountName = "IHIL Engineering Ltd",
                    accountNumber = "0123456789",
                    momoNumber = "0244123456"
                )
            )
        )
        assertEquals(true, settings.canPayWithPaystack)
        assertEquals(true, settings.canPayManually)
        assertEquals("0123456789", settings.manualPayment.accountNumber)
        assertEquals("0244123456", settings.manualPayment.momoNumber)
        assertEquals("233244123456", settings.supportWhatsAppNumber)
        assertEquals(true, settings.hasWhatsAppSupport)
    }

    @Test
    fun mapBusinessSettings_offersBothPaymentMethodsWithoutManualDetails() {
        val settings = LaravelApiMapper.mapBusinessSettings(
            ApiBusinessSettingsDto(currency = "GHS")
        )
        assertEquals(true, settings.canPayWithPaystack)
        assertEquals(true, settings.canPayManually)
    }
}
