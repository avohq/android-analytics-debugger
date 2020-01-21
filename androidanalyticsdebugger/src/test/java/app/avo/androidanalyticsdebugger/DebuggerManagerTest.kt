package app.avo.androidanalyticsdebugger

import android.app.Activity
import android.content.res.Resources
import app.avo.androidanalyticsdebugger.debuggerview.DebuggerViewContainer
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import java.lang.ref.WeakReference
import java.util.*
import org.junit.Assert.*
import android.view.WindowManager
import android.widget.TextView
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before


class DebuggerManagerTest {

    private var debuggerManager = DebuggerManager()

    @Before
    fun setUp() {
        DebuggerManager.events.clear()
    }

    @Test
    fun newEventIsSaved() {
        // Given
        val eventProps = ArrayList<Map<String, String>>()

        eventProps.add(object : HashMap<String, String>() {
            init {
                put("id", "event prop id")
                put("name", "Post Type")
                put("value", "gif")
            }
        })
        eventProps.add(object : HashMap<String, String>() {
            init {
                put("id", "good event id")
                put("name", "Comment Id")
                put("value", "sdfdf2")
            }
        })
        val messages = ArrayList<Map<String, String>>()
        messages.add(object : HashMap<String, String>() {
            init {
                put("tag", "tagValue")
                put("propertyId", "event prop id")
                put("message", "Post Type should match one of: GIF, Image, Video or Quote but you provided gif.")
                put("allowedTypes", "GIF,Image,Video,Quote")
                put("providedType", "gif")
            }
        })
        val event = DebuggerEventItem("id", 45444, "name", messages, eventProps, null)

        // When
        debuggerManager.publishEvent(event)

        // Then
        assert(event == DebuggerManager.events[0])
    }

    @Test
    fun eventsAreSortedDescendingByTime() {
        // Given
        val firstEvent = createBasicEvent()
        val secondEvent = DebuggerEventItem("id", 2, "name",
                null, null, null)

        // When
        debuggerManager.publishEvent(secondEvent)
        debuggerManager.publishEvent(firstEvent)

        // Then
        assertEquals(secondEvent, DebuggerManager.events[0])
        assertEquals(firstEvent, DebuggerManager.events[1])
    }

    @Test
    fun newEventIsPostedToCurrentDebugger() {
        // Given
        val mockDebugger = mock<DebuggerViewContainer>()
        DebuggerManager.debuggerViewContainerRef = WeakReference(mockDebugger)

        val event = createBasicEvent()

        // When
        debuggerManager.publishEvent(event)

        // Then
        verify(mockDebugger).showEvent(event)
    }

    @Test
    fun newEventIsPostedToEventUpdateListener() {
        // Given
        DebuggerManager.eventUpdateListener = mock()

        val event = createBasicEvent()

        // When
        debuggerManager.publishEvent(event)

        // Then
        verify(DebuggerManager.eventUpdateListener).onNewEvent(event)
    }

    @Test
    fun publishWithSeparateArgumentsCallsPublishWithSameEvent() {
        // Given
        debuggerManager = spy(debuggerManager)

        // When
        debuggerManager.publishEvent("id", 1, "name",
                null, null, null)

        // Then
        val event = createBasicEvent()
        verify(debuggerManager).publishEvent(event)
    }

    @Test
    fun isEnabledWhenDebuggerIsPresent() {
        // Given
        val mockDebugger = mock<DebuggerViewContainer>()
        DebuggerManager.debuggerViewContainerRef = WeakReference(mockDebugger)

        // Then
        assert(debuggerManager.isEnabled)
    }

    @Test
    fun isNotEnabledWhenDebuggerIsNotPresent() {
        // Given
        DebuggerManager.debuggerViewContainerRef = WeakReference(null)


        // Then
        assertFalse(debuggerManager.isEnabled)
    }

    @Test
    fun showsBubbleDebuggerWithAccumulatedEvents() {
        // Given
        // Mocks setup
        val counter = mock<TextView>()
        val bubbleViewLayoutInflater = bubbleDebuggerLayoutInflater(mock(), mock(), counter)
        val res = mock<Resources>()
        val mockWindowManager = mock<WindowManager>()
        val mockActivity = mock<Activity>()
        whenever(mockActivity.windowManager).thenReturn(mockWindowManager)
        whenever(mockActivity.resources).thenReturn(res)
        whenever(mockActivity.layoutInflater).thenReturn(bubbleViewLayoutInflater)

        // A number of events sent before the debugger is shown
        for (i in (0..10L)) {
            DebuggerManager.events.add(createBasicEvent(i))
        }

        // When
        debuggerManager.showDebugger(mockActivity, DebuggerMode.bubble)

        // Then
        verify(counter).text = "10"
    }

    @Test
    fun showsBarDebuggerWithLastEvent() {
        // Given
        // Mocks setup
        val timestamp = mock<TextView>()
        val eventName = mock<TextView>()
        val bubbleViewLayoutInflater = barDebuggerLayoutInflater(mock(), mock(), mock(), timestamp,
                eventName)
        val res = mock<Resources>()
        val mockWindowManager = mock<WindowManager>()
        val mockActivity = mock<Activity>()
        whenever(mockActivity.windowManager).thenReturn(mockWindowManager)
        whenever(mockActivity.resources).thenReturn(res)
        whenever(mockActivity.layoutInflater).thenReturn(bubbleViewLayoutInflater)

        // A number of events is sent before the debugger is shown
        for (i in (0..10L)) {
            DebuggerManager.events.add(createBasicEvent(i))
        }
        val lastEvent = DebuggerEventItem("id", System.currentTimeMillis(), "last event name",
                null, null, null)
        DebuggerManager.events.add(lastEvent)

        // When
        debuggerManager.showDebugger(mockActivity, DebuggerMode.bar)

        // Then
        verify(timestamp).text = Util.timeString(lastEvent.timestamp)
        verify(eventName).text = lastEvent.name
    }

    @Test
    fun savesSchemaId() {
        // Given
        val schemaId = "test schema id"

        // When
        debuggerManager.setSchemaId(schemaId)

        // Then
       assertEquals(schemaId, debuggerManager.schemaId)
    }
}