package app.avo.androidanalyticsdebugger

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.avo.androidanalyticsdebugger.debuggerview.BubbleViewContainer
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.junit.Assert.*

class BubbleViewContainerTest {

    private lateinit var bubbleViewContainer: BubbleViewContainer

    private val mockContainer = mock<View>()
    private val mockBubbleView = mock<ImageView>()
    private val mockCounterView = mock<TextView>()
    private lateinit var mockLayoutInflater: LayoutInflater

    @Before
    fun prepare() {
        mockLayoutInflater = bubbleDebuggerLayoutInflater(mockContainer, mockBubbleView, mockCounterView)

        bubbleViewContainer = spy(BubbleViewContainer(mockLayoutInflater))
    }

    @Test
    fun errorResetsOnClick() {
        // Given
        val res = mock<Resources>()
        whenever(res.getColor(R.color.background)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.background, null)).thenReturn(Color.WHITE)
        whenever(mockContainer.resources).thenReturn(res)

        // When
        bubbleViewContainer.onClickListener.onClick(mock())

        // Then
        verify(mockBubbleView).setImageResource(R.drawable.avo_bubble)
        verify(mockCounterView).setBackgroundResource(R.drawable.green_oval)
        verify(mockCounterView).setTextColor(Color.WHITE)
    }

    @Test
    fun counterResetsOnClick() {
        // Given
        val res = mock<Resources>()
        whenever(res.getColor(R.color.background)).thenReturn(Color.WHITE)
        whenever(res.getColor(R.color.background, null)).thenReturn(Color.WHITE)
        whenever(mockContainer.resources).thenReturn(res)

        // When
        bubbleViewContainer.onClickListener.onClick(mock())

        // Then
        verify(mockCounterView).text = "0"
    }

    @Test
    fun showsSuccessfulEvent() {
        // When
        bubbleViewContainer.showEvent(DebuggerEventItem())

        // Then
        verify(mockCounterView).text = "1"

        verify(mockBubbleView, never()).setImageResource(anyOrNull())
        verify(mockCounterView, never()).setBackgroundResource(anyOrNull())
        verify(mockCounterView, never()).setTextColor(anyInt())
    }

    @Test
    fun showsErrorEvent() {
        // Given
        val res = mock<Resources>()
        whenever(res.getColor(R.color.error)).thenReturn(Color.RED)
        whenever(res.getColor(R.color.error, null)).thenReturn(Color.RED)
        whenever(mockContainer.resources).thenReturn(res)

        val eventWithError = DebuggerEventItem("", 1, "",
                mutableListOf(mutableMapOf("tag" to "tag", "propertyId" to "propId", "message" to "mes")),
                null, null)

        // When
        bubbleViewContainer.showEvent(eventWithError)

        // Then
        verify(mockCounterView).text = "1"

        verify(mockBubbleView).setImageResource(R.drawable.avo_bubble_error)
        verify(mockCounterView).setBackgroundResource(R.drawable.white_oval)
        verify(mockCounterView).setTextColor(Color.RED)
    }

    @Test
    fun counterCountsOver9000() {
        // When
        for (i in (0..9001)) {
            bubbleViewContainer.showEvent(DebuggerEventItem())
        }

        // Then
        verify(mockCounterView).text = "9001"
    }

    @Test
    fun getViewReturnsContainerView() {
        assertEquals(mockContainer, bubbleViewContainer.view)
    }
}