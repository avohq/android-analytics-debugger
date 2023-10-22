package app.avo.androidanalyticsdebugger.debuggereventslist

import android.view.View
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.Util
import app.avo.androidanalyticsdebugger.createBasicEvent
import app.avo.androidanalyticsdebugger.createErrorEvent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class DebuggerEventsListAdapterTest {

    private lateinit var sut: DebuggerEventsListAdapter

    @Before
    fun beforeEach() {
        DebuggerManager.events.clear();
    }

    @Test
    fun onlyLastEventAndErrorsAreExpended() {
        // Given
        val errorEvent = createErrorEvent(1)
        val lastEvent = createBasicEvent(4)
        DebuggerManager.events.add(errorEvent)
        DebuggerManager.events.add(createBasicEvent(2))
        DebuggerManager.events.add(createBasicEvent(3))
        DebuggerManager.events.add(lastEvent)

        // When
        sut = DebuggerEventsListAdapter()

        // Then
        assertEquals(2, sut.expendedEvents.size)
        assertTrue(sut.expendedEvents.contains(errorEvent))
        assertTrue(sut.expendedEvents.contains(lastEvent))
    }

    @Test
    fun viewHolderBindsEventNameAndTimestamp() {
        // Given
        val itemView = mock<View>()
        whenever(itemView.resources).thenReturn(mock())
        val viewHolder = DebuggerEventsListAdapter.DebuggerEventViewHolder(itemView)
        viewHolder.eventName = mock()
        viewHolder.timestamp = mock()
        viewHolder.expendedContent = mock()
        viewHolder.expendButton = mock()
        viewHolder.successIcon = mock()
        whenever(viewHolder.expendedContent.layoutParams).thenReturn(mock())

        val event = createBasicEvent(4)
        DebuggerManager.events.add(event)
        sut = DebuggerEventsListAdapter()

        // When
        sut.onBindViewHolder(viewHolder, 0)

        // Then
        verify(viewHolder.eventName).text = event.name
        verify(viewHolder.timestamp).text = Util.timeString(event.timestamp)
        verify(viewHolder.expendedContent).removeAllViews()
    }

    @Test
    fun usesDebuggerManagerSortingByDefault() {
        // Given
        val errorEvent = createErrorEvent(1)
        val lastEvent = createBasicEvent(4)
        DebuggerManager.events.add(errorEvent)
        DebuggerManager.events.add(createBasicEvent(2))
        DebuggerManager.events.add(createBasicEvent(3))
        DebuggerManager.events.add(lastEvent)

        // When
        sut = DebuggerEventsListAdapter()

        // Then
        assertEquals(DebuggerManager.events.size(), sut.events.size())
        assertEquals(DebuggerManager.events[0], sut.events[0])
        assertEquals(DebuggerManager.events[1], sut.events[1])
        assertEquals(DebuggerManager.events[2], sut.events[2])
        assertEquals(DebuggerManager.events[3], sut.events[3])
    }
}