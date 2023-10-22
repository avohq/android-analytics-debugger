package app.avo.androidanalyticsdebugger.debuggereventslist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.R
import app.avo.androidanalyticsdebugger.createBasicEvent
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
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
    fun sortsPropertiesAlphabetically() {
        // Given
        val event = DebuggerEventItem(
            "id",
            1,
            "name",
            listOf(),
            listOf(
                mapOf(Pair("id", "0"), Pair("name", "B prop"), Pair("value", "val0")),
                mapOf(Pair("id", "1"), Pair("name", "A prop"), Pair("value", "val1"))
            ),
            listOf(
                mapOf(Pair("id", "2"), Pair("name", "B User prop"), Pair("value", "valU0")),
                mapOf(Pair("id", "3"), Pair("name", "A User prop"), Pair("value", "valU1"))
            )
        )
        DebuggerManager.events.add(event)
        val controller = buildActivity(_DebuggerEventsListActivity::class.java)
        controller.create(null)
        val activity = controller.get()
        val viewHolder = activity.adapter.onCreateViewHolder(LinearLayout(activity), 0)

        // When
        activity.adapter.onBindViewHolder(viewHolder, 0)

        // Then
        assertEquals("A prop", (viewHolder.expendedContent.getChildAt(0).findViewById(R.id.prop_name) as TextView).text)
        assertEquals("B prop", (viewHolder.expendedContent.getChildAt(2).findViewById(R.id.prop_name) as TextView).text)
        assertEquals("A User prop", (viewHolder.expendedContent.getChildAt(4).findViewById(R.id.prop_name) as TextView).text)
        assertEquals("B User prop", (viewHolder.expendedContent.getChildAt(6).findViewById(R.id.prop_name) as TextView).text)
    }
}