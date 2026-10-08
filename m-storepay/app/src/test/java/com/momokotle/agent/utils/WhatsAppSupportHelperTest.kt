package com.momokotle.agent.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WhatsAppSupportHelperTest {

    @Test
    fun normalizeDigits_ghanaLocalAndInternational() {
        assertEquals("233244123456", WhatsAppSupportHelper.normalizeDigits("0244123456"))
        assertEquals("233244123456", WhatsAppSupportHelper.normalizeDigits("+233 24 412 3456"))
        assertEquals("233244123456", WhatsAppSupportHelper.normalizeDigits("233244123456"))
    }

    @Test
    fun normalizeDigits_rejectsInvalid() {
        assertNull(WhatsAppSupportHelper.normalizeDigits(""))
        assertNull(WhatsAppSupportHelper.normalizeDigits("123"))
        assertNull(WhatsAppSupportHelper.normalizeDigits("abc"))
    }

    @Test
    fun chatUrl_usesOfficialWaMeFormat() {
        assertEquals(
            "https://wa.me/233244123456",
            WhatsAppSupportHelper.chatUrl("233244123456")
        )
        assertEquals(
            "https://api.whatsapp.com/send?phone=233244123456",
            WhatsAppSupportHelper.apiSendUrl("233244123456")
        )
    }
}
