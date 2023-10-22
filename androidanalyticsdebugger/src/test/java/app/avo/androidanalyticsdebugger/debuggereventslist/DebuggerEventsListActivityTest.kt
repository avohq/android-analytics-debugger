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
    fun sortButtonSortsAlphabeticallyOnFirstClickAndDefaultOnSecond() {
        // Given
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)
        val bEarlierEvent = createErrorEvent(1, name = "b")
        val bLaterEvent = createBasicEvent(4, name = "b")
        val aEvent = createBasicEvent(2, name = "a")
        val cEvent = createBasicEvent(3, name = "c")
        DebuggerManager.events.add(bLaterEvent)
        DebuggerManager.events.add(aEvent)
        DebuggerManager.events.add(cEvent)
        DebuggerManager.events.add(bEarlierEvent)
        controller.create(null)
        val closeButton = controller.get().findViewById<ImageView>(R.id.sort_button)
        val resources = controller.get().resources

        // When
        closeButton.performClick()

        // Then
        assertEquals(aEvent, controller.get().adapter.events[0])
        assertEquals(bLaterEvent, controller.get().adapter.events[1])
        assertEquals(bEarlierEvent, controller.get().adapter.events[2])
        assertEquals(cEvent, controller.get().adapter.events[3])
        assertEquals(resources.getDrawable(android.R.drawable.ic_menu_sort_by_size), closeButton.drawable)

        // When
        closeButton.performClick()

        // Then
        assertEquals(bLaterEvent, controller.get().adapter.events[0])
        assertEquals(cEvent, controller.get().adapter.events[1])
        assertEquals(aEvent, controller.get().adapter.events[2])
        assertEquals(bEarlierEvent, controller.get().adapter.events[3])
        assertEquals(resources.getDrawable(android.R.drawable.ic_menu_sort_alphabetically), closeButton.drawable)
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
}