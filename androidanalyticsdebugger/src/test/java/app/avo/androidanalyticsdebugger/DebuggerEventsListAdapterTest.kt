package app.avo.androidanalyticsdebugger

import android.view.View
import app.avo.androidanalyticsdebugger.debuggereventslist.DebuggerEventsListAdapter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.Assert.*

class DebuggerEventsListAdapterTest {

    lateinit var sut: DebuggerEventsListAdapter

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
}