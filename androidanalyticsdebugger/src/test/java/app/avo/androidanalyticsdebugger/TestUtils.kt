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
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Timestamp


@Throws(Exception::class)
fun setFinalStatic(field: Field, newValue: Any) {
    field.setAccessible(true)

    val modifiersField = Field::class.java!!.getDeclaredField("modifiers")
    modifiersField.setAccessible(true)
    modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())

    field.set(null, newValue)
}

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

fun createBasicEvent(timestamp: Long = 1): DebuggerEventItem {
    return DebuggerEventItem("id", timestamp, "name",
            null, null, null)
}