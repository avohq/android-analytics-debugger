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
        sut = DebuggerEventsListAdapter(Sorting.Default)

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
        sut = DebuggerEventsListAdapter(Sorting.Default)

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
        sut = DebuggerEventsListAdapter(Sorting.Default)

        // Then
        assertEquals(DebuggerManager.events, sut.events)
    }

    @Test
    fun usesNameSortingWhenChosen() {
        // Given
        val bEarlierEvent = createErrorEvent(1, name = "b")
        val bLaterEvent = createBasicEvent(4, name = "b")
        val aEvent = createBasicEvent(2, name = "a")
        val cEvent = createBasicEvent(3, name = "c")
        DebuggerManager.events.add(bLaterEvent)
        DebuggerManager.events.add(aEvent)
        DebuggerManager.events.add(cEvent)
        DebuggerManager.events.add(bEarlierEvent)

        // When
        sut = DebuggerEventsListAdapter(Sorting.Alphabetical)

        // Then
        assertEquals(aEvent, sut.events[0])
        assertEquals(bLaterEvent, sut.events[1])
        assertEquals(bEarlierEvent, sut.events[2])
        assertEquals(cEvent, sut.events[3])
    }
}