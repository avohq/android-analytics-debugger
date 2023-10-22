package app.avo.androidanalyticsdebugger.debuggereventslist

import android.os.Build
import android.view.View
import android.widget.ImageView
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.R
import app.avo.androidanalyticsdebugger.createBasicEvent
import app.avo.androidanalyticsdebugger.createErrorEvent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

        // When
        activity.adapter.filter("mid")

        // Then
        assertEquals(midnightEvent, activity.adapter.events[0])
        assertEquals(middayEvent, activity.adapter.events[1])
        assertEquals(2, activity.adapter.events.size())
    }
}