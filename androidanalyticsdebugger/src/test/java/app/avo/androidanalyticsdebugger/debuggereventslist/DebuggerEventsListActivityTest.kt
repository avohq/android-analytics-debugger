package app.avo.androidanalyticsdebugger.debuggereventslist

import android.os.Build
import android.view.View
import android.widget.EditText
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.R
import app.avo.androidanalyticsdebugger.createBasicEvent
import app.avo.androidanalyticsdebugger.createErrorEvent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DebuggerEventsListActivityTest {

    @Test
    fun closeButtonClosesActivity() {
        // Given
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)

        // When
        controller.create(null)
        controller.get().findViewById<View>(R.id.close_button).performClick()

        // Then
        assertEquals(true, controller.get().isFinishing)
    }

    @Test
    fun newEventTriggersAdapterRefresh() {
        // Given
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)
        controller.create(null)
        controller.get().adapter = mock()
        val event = createBasicEvent(1)

        // When
        DebuggerManager.eventUpdateListener.onNewEvent(event)

        // Then
        verify(controller.get().adapter).onNewItem(event)
    }

    @Test
    fun clearsEventsListenerOnDestroy() {
        // Given
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)
        controller.create(null)
        controller.get().adapter = mock()

        // When
        controller.destroy()

        // Then
        assertNull(DebuggerManager.eventUpdateListener)
    }

    @Test
    fun showsSubsetOfEventsBasedOnFilter() {
        val sunsetEvent = createErrorEvent(3, name = "sunset")
        val midnightEvent = createBasicEvent(4, name = "midnight")
        val sunriseEvent = createBasicEvent(1, name = "sunrise")
        val middayEvent = createBasicEvent(2, name = "midday")
        DebuggerManager.events.add(sunsetEvent)
        DebuggerManager.events.add(midnightEvent)
        DebuggerManager.events.add(sunriseEvent)
        DebuggerManager.events.add(middayEvent)

        val controller = buildActivity(_DebuggerEventsListActivity::class.java)
        controller.create(null)
        val activity = controller.get()

        // When filter is set
        activity.findViewById<View>(R.id.filter_input_toggle).performClick()
        activity.findViewById<EditText>(R.id.filter_input).setText("mId")

        // Then the content is filtered
        assertEquals(midnightEvent, activity.adapter.events[0])
        assertEquals(middayEvent, activity.adapter.events[1])
        assertEquals(2, activity.adapter.events.size())

        // When filter is hidden
        activity.findViewById<View>(R.id.filter_input_toggle).performClick()

        // Then all events are visible
        assertEquals(4, activity.adapter.events.size())

        // When filter is shown back
        activity.findViewById<View>(R.id.filter_input_toggle).performClick()

        // The events are filtered again
        assertEquals(midnightEvent, activity.adapter.events[0])
        assertEquals(middayEvent, activity.adapter.events[1])
        assertEquals(2, activity.adapter.events.size())
    }

    @Test
    fun triggersFilterEditText() {
        // Given
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)

        controller.create(null)
        val activity = controller.get()
        assertEquals(View.GONE, activity.findViewById<View>(R.id.filter_input).visibility)

        // When
        activity.findViewById<View>(R.id.filter_input_toggle).performClick()

        // Then
        assertEquals(View.VISIBLE, activity.findViewById<View>(R.id.filter_input).visibility)
    }
}