package com.momokotle.agent.database

import com.momokotle.agent.database.entity.encodeCustomFlowSteps
import com.momokotle.agent.database.entity.parseCustomFlowSteps
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomFlowStepsCodecTest {

    @Test
    fun roundTrip_preservesOrderedSteps() {
        val steps = listOf("3", "1", "phone", "amount")
        val json = encodeCustomFlowSteps(steps)
        assertEquals("""["3","1","phone","amount"]""", json)
        assertEquals(steps, parseCustomFlowSteps(json))
    }

    @Test
    fun parse_skipsBlanksAndNonStrings() {
        assertEquals(
            listOf("1", "2"),
            parseCustomFlowSteps("""["1","","2",null]""")
        )
        assertEquals(emptyList<String>(), parseCustomFlowSteps("not-json"))
        assertEquals(emptyList<String>(), parseCustomFlowSteps("[]"))
    }
}
