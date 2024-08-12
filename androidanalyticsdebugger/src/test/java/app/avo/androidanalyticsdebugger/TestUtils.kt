package app.avo.androidanalyticsdebugger

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.ArgumentMatchers

fun bubbleDebuggerLayoutInflater(mockContainer: View, mockBubbleView: ImageView,
                                 mockCounterView: TextView): LayoutInflater {

    whenever(mockContainer.findViewById<ImageView>(R.id.bubble)).thenReturn(mockBubbleView)
    whenever(mockContainer.findViewById<TextView>(R.id.counter)).thenReturn(mockCounterView)

    val mockLayoutInflater = mock<LayoutInflater>()
    whenever(mockLayoutInflater.inflate(ArgumentMatchers.anyInt(), anyOrNull())).thenReturn(mockContainer)

    return mockLayoutInflater
}

fun barDebuggerLayoutInflater(mockContainer: View, mockSuccessIcon: ImageView,
                              mockDrugHandle: ImageView,
                              mockTimestamp: TextView, mockEventName: TextView): LayoutInflater {

    whenever(mockContainer.findViewById<TextView>(R.id.timestamp)).thenReturn(mockTimestamp)
    whenever(mockContainer.findViewById<TextView>(R.id.event_name)).thenReturn(mockEventName)
    whenever(mockContainer.findViewById<ImageView>(R.id.success_icon)).thenReturn(mockSuccessIcon)
    whenever(mockContainer.findViewById<ImageView>(R.id.drag_handle)).thenReturn(mockDrugHandle)

    val mockLayoutInflater = mock<LayoutInflater>()
    whenever(mockLayoutInflater.inflate(ArgumentMatchers.anyInt(), anyOrNull())).thenReturn(mockContainer)

    return mockLayoutInflater
}

fun createBasicEvent(timestamp: Long = 1, name: String = "name"): DebuggerEventItem {
    return DebuggerEventItem("id", timestamp, name,
            null, null, null)
}

fun createErrorEvent(timestamp: Long = 1, name: String = "name"): DebuggerEventItem {
    return DebuggerEventItem("id", timestamp, name,
            listOf(mapOf(Pair("tag", "tag"), Pair("propertyId", "prop"), Pair("message", "mes"))), null, null)
}