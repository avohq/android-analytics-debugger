package app.avo.androidanalyticsdebugger

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import org.junit.Test
import org.junit.Assert.*

class UtilTest {

    @Test
    fun eventHasErrorsPositiveTest() {

        // Given
        val eventWithError = DebuggerEventItem("", 1, "",
                mutableListOf(mutableMapOf("tag" to "tag", "propertyId" to "propId", "message" to "mes")),
                null, null)

        // Then
        assertTrue(Util.eventHaveErrors(eventWithError))
    }

    @Test
    fun eventHasErrorsNegativeTest() {

        // Given
        val eventWithError = DebuggerEventItem("", 1, "",
                mutableListOf(),null, null)

        // Then
        assertFalse(Util.eventHaveErrors(eventWithError))
    }
}