package app.avo.androidanalyticsdebugger

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.avo.androidanalyticsdebugger.debuggerview.BarViewContainer
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class BarViewContainerTest {
    private lateinit var barViewContainer: BarViewContainer

    private val mockContainer = mock<View>()
    private val mockTimestamp = mock<TextView>()
    private val mockEventName = mock<TextView>()
    private val mockSuccessIcon = mock<ImageView>()
    private val mockDragHandle = mock<ImageView>()

    private lateinit var mockLayoutInflater: LayoutInflater

    @Before
    fun prepare() {
        mockLayoutInflater = barDebuggerLayoutInflater(mockContainer, mockSuccessIcon,
                mockDragHandle, mockTimestamp, mockEventName)

        barViewContainer = spy(BarViewContainer(mockLayoutInflater))
    }

    @Test
    fun errorResetsOnClick() {
        // Given
        val res = mock<Resources>()
        whenever(res.getColor(R.color.background)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.background, null)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.foreground)).thenReturn(Color.BLACK)
        whenever(res.getColor(R.color.foreground, null)).thenReturn(Color.BLACK)
        whenever(res.getColor(R.color.foregroundLight)).thenReturn(Color.GRAY)
        whenever(res.getColor(R.color.foregroundLight, null)).thenReturn(Color.GRAY)

        whenever(mockContainer.resources).thenReturn(res)

        // When
        barViewContainer.onClickListener.onClick(mock())

        // Then
        verify(mockSuccessIcon).setImageResource(R.drawable.tick)
        verify(mockDragHandle).setImageResource(R.drawable.drag_handle_grey)
        verify(mockContainer).setBackgroundResource(R.color.background)
        verify(mockTimestamp).setTextColor(Color.GRAY)
        verify(mockEventName).setTextColor(Color.BLACK)
    }

    @Test
    fun showsSuccessfulEvent() {
        // Given
        val event = createBasicEvent()

        // When
        barViewContainer.showEvent(event)

        // Then
        verify(mockTimestamp).text = Util.timeString(event.timestamp)
        verify(mockEventName).text = event.name

        verify(mockSuccessIcon, never()).setImageResource(anyOrNull())
        verify(mockDragHandle, never()).setImageResource(anyOrNull())
        verify(mockContainer, never()).setBackgroundResource(anyOrNull())
        verify(mockTimestamp, never()).setTextColor(ArgumentMatchers.anyInt())
        verify(mockEventName, never()).setTextColor(ArgumentMatchers.anyInt())
    }

    @Test
    fun showsErrorEvent() {
        // Given
        val res = mock<Resources>()
        whenever(res.getColor(R.color.background)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.background, null)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.foregroundLighter)).thenReturn(Color.GRAY)
        whenever(res.getColor(R.color.foregroundLighter, null)).thenReturn(Color.GRAY)

        whenever(mockContainer.resources).thenReturn(res)

        val eventWithError = DebuggerEventItem("id000", 1, "errorEvent",
                mutableListOf(mutableMapOf("tag" to "tag", "propertyId" to "propId", "message" to "mes")),
                null, null)

        // When
        barViewContainer.showEvent(eventWithError)

        // Then
        verify(mockTimestamp).text = Util.timeString(eventWithError.timestamp)
        verify(mockEventName).text = eventWithError.name

        verify(mockSuccessIcon).setImageResource(R.drawable.warning)
        verify(mockDragHandle).setImageResource(R.drawable.drag_handle_white)
        verify(mockContainer).setBackgroundResource(R.color.error)
        verify(mockTimestamp).setTextColor(Color.GRAY)
        verify(mockEventName).setTextColor(Color.WHITE)
    }

    @Test
    fun getViewReturnsContainerView() {
        Assert.assertEquals(mockContainer, barViewContainer.view)
    }
}