package app.avo.androidanalyticsdebugger

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import org.junit.Test
import org.junit.Assert.*

class DebuggerEventItemTest {

    @Test
    fun messageRequiresTagPropertyIdAndMessage() {

        // Given
        val properMessage0 = mapOf("tag" to "tag", "propertyId" to "propId", "message" to "mess")
        val improperMessage0 = mapOf("propertyId" to "propId", "message" to "mess")
        val improperMessage1 = mapOf("tag" to "tag", "message" to "mess")
        val improperMessage2 = mapOf("tag" to "tag", "propertyId" to "propId")
        val properMessage1 = mapOf("tag" to "tag", "propertyId" to "propId", "message" to "mess",
                "somerandomprop" to "rand", "providedType" to "someType")

        // When
        val debuggerEventItem = DebuggerEventItem("id", 1, "name",
                listOf(properMessage0, improperMessage0, improperMessage1, improperMessage2,
                        properMessage1), null, null)

        // Then
        assertEquals(2, debuggerEventItem.messages.size)

        assertEquals(properMessage0["propertyId"], debuggerEventItem.messages[0].propertyId)
        assertEquals(properMessage0["message"], debuggerEventItem.messages[0].message)
        assertEquals(properMessage0["providedType"], debuggerEventItem.messages[0].providedType)

        assertEquals(properMessage1["propertyId"], debuggerEventItem.messages[1].propertyId)
        assertEquals(properMessage1["message"], debuggerEventItem.messages[1].message)
        assertEquals(properMessage1["providedType"], debuggerEventItem.messages[1].providedType)
    }

    @Test
    fun messageCommaSeparatedAllowedTypesAreProperlyParsed() {
        // Given
        val message = mapOf("tag" to "tag", "propertyId" to "propId", "message" to "mess",
                "allowedTypes" to "type0,type1,type2")

        // When
        val debuggerEventItem = DebuggerEventItem("id", 1, "name",
                listOf(message), null, null)

        // Then
        assertEquals(3, debuggerEventItem.messages[0].allowedTypes?.size)
        assertEquals("type0", debuggerEventItem.messages[0].allowedTypes?.get(0))
        assertEquals("type1", debuggerEventItem.messages[0].allowedTypes?.get(1))
        assertEquals("type2", debuggerEventItem.messages[0].allowedTypes?.get(2))
    }

    @Test
    fun eventPropRequiresIdNameAndValue() {

        // Given
        val properProp0 = mapOf("id" to "id", "name" to "name", "value" to "val")
        val improperProp0 = mapOf("name" to "name", "value" to "val")
        val improperProp1 = mapOf("id" to "id", "value" to "val")
        val improperProp2 = mapOf("id" to "id", "name" to "name")
        val properProp1 = mapOf("id" to "id", "name" to "name", "value" to "val",
                "somerandomprop" to "rand")

        // When
        val debuggerEventItem = DebuggerEventItem("id", 1, "name",
                null, listOf(properProp0, improperProp0, improperProp1, improperProp2,
                properProp1), null)

        // Then
        assertEquals(2, debuggerEventItem.eventProps.size)

        assertEquals(properProp0["id"], debuggerEventItem.eventProps[0].id)
        assertEquals(properProp0["name"], debuggerEventItem.eventProps[0].name)
        assertEquals(properProp0["value"], debuggerEventItem.eventProps[0].value)

        assertEquals(properProp1["id"], debuggerEventItem.eventProps[1].id)
        assertEquals(properProp1["name"], debuggerEventItem.eventProps[1].name)
        assertEquals(properProp1["value"], debuggerEventItem.eventProps[1].value)
    }

    @Test
    fun userPropRequiresIdNameAndValue() {

        // Given
        val properProp0 = mapOf("id" to "id", "name" to "name", "value" to "val")
        val improperProp0 = mapOf("name" to "name", "value" to "val")
        val improperProp1 = mapOf("id" to "id", "value" to "val")
        val improperProp2 = mapOf("id" to "id", "name" to "name")
        val properProp1 = mapOf("id" to "id", "name" to "name", "value" to "val",
                "somerandomprop" to "rand")

        // When
        val debuggerEventItem = DebuggerEventItem("id", 1, "name",
                null, null,
                listOf(properProp0, improperProp0, improperProp1, improperProp2, properProp1))

        // Then
        assertEquals(2, debuggerEventItem.userProps.size)

        assertEquals(properProp0["id"], debuggerEventItem.userProps[0].id)
        assertEquals(properProp0["name"], debuggerEventItem.userProps[0].name)
        assertEquals(properProp0["value"], debuggerEventItem.userProps[0].value)

        assertEquals(properProp1["id"], debuggerEventItem.userProps[1].id)
        assertEquals(properProp1["name"], debuggerEventItem.userProps[1].name)
        assertEquals(properProp1["value"], debuggerEventItem.userProps[1].value)
    }

    @Test
    fun eventItemsWithSameFieldsAreEqual() {
        // Given
        val message = mapOf("tag" to "tag", "propertyId" to "propId", "message" to "mess")
        val properProp0 = mapOf("id" to "id", "name" to "name", "value" to "val")
        val properProp1 = mapOf("id" to "id", "name" to "name", "value" to "val",
                "somerandomprop" to "rand")

        // When
        val debuggerEventItem0 = DebuggerEventItem("id", 1, "name",
                listOf(message), listOf(properProp0),
                listOf(properProp1))
        val debuggerEventItem1 = DebuggerEventItem("id", 1, "name",
                listOf(message), listOf(properProp0),
                listOf(properProp1))

        // Then
        assertEquals(debuggerEventItem0, debuggerEventItem1)
    }
}