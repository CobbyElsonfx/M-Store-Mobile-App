package com.momokotle.agent.ussd

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UssdWindowGateTest {

    @Test
    fun rejectsOwnAppPackage() {
        assertTrue(UssdWindowGate.isOwnAppPackage("com.momokotle.agent"))
        assertFalse(UssdWindowGate.mayAutomate("com.momokotle.agent"))
        assertFalse(UssdWindowGate.mayAutomate("com.momokotle.agent.debug"))
    }

    @Test
    fun acceptsKnownDialerPackages() {
        assertTrue(UssdWindowGate.mayAutomate("com.android.phone"))
        assertTrue(UssdWindowGate.mayAutomate("com.google.android.dialer"))
        assertTrue(UssdWindowGate.mayAutomate("com.samsung.android.dialer"))
        assertTrue(UssdWindowGate.mayAutomate("com.samsung.android.app.telephonyui"))
    }

    @Test
    fun rejectsUnrelatedApps() {
        assertFalse(UssdWindowGate.mayAutomate("com.whatsapp"))
        assertFalse(UssdWindowGate.mayAutomate(null))
        assertFalse(UssdWindowGate.mayAutomate(""))
    }
}
