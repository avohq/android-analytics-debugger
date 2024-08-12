package app.avo.musicplayerexample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.DebuggerMode
import app.avo.androidanalyticsdebugger.EventProperty
import app.avo.androidanalyticsdebugger.PropertyError
import sh.avo.Avo
import java.util.*
import java.util.concurrent.TimeUnit

class ExampleMusicPlayerActivity : AppCompatActivity() {

    private val logic = ExampleMusicPlayerLogic()

    private var timer: Timer? = null

    private val debuggerManager: DebuggerManager
        get() = (application as MusicPlayerExampleApplication).debugger

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_music_player)

        initialState()

        initShowDebuggerButtons()

        findViewById<View>(R.id.play_pause).setOnClickListener {
            onPlayPauseClick()
        }
        findViewById<View>(R.id.next_track).setOnClickListener {
            onNextTrackClick()
        }
        findViewById<View>(R.id.prev_track).setOnClickListener {
            onPrevTrackClick()
        }

        debuggerManager.publishEvent(/*timestamp =*/ System.currentTimeMillis(), /*name =*/ "Test custom event",
                /*properties =*/ listOf(EventProperty(/*id =*/ "id0", /*name =*/ "property0", "value"),
                    EventProperty(/*id =*/ "id1", /*name =*/ "property1", "value")),
                /*errors =*/ listOf(PropertyError(/*propertyId =*/ "id0", /*message =*/ "Error in property with id 'id0'")))
    }

    private fun initShowDebuggerButtons() {
        findViewById<View>(R.id.show_bubble_debugger).setOnClickListener {
            debuggerManager.showDebugger(this, DebuggerMode.bubble)
        }
        findViewById<View>(R.id.show_bar_debugger).setOnClickListener {
            debuggerManager.showDebugger(this, DebuggerMode.bar)
        }
        findViewById<View>(R.id.hide_debugger).setOnClickListener {
            debuggerManager.hideDebugger(this)
        }
    }

    private fun initialState() {
        logic.loadCurrentTrack(applicationContext)
        showCurrentTrack()
        findViewById<View>(R.id.prev_track).visibility = View.INVISIBLE
    }

    private fun onPrevTrackClick() {
        val currentTrackBeforeSwitching = logic.currentTrackName()
        val upcomingTrackBeforeSwitching = logic.prevTrackName()

        if (logic.loadPrevTrack(applicationContext)) {
            showCurrentTrack()
            manageNavigationButtonsVisibilityOnPrev()

            Avo.playPreviousTrack(currentTrackBeforeSwitching, upcomingTrackBeforeSwitching)
        }
    }

    private fun onNextTrackClick() {
        val currentTrackBeforeSwitching = logic.currentTrackName()
        val upcomingTrackBeforeSwitching = logic.nextTrackName()

        if (logic.loadNextTrack(applicationContext)) {
            showCurrentTrack()
            manageNavigationButtonsVisibilityOnNext()

            Avo.playNextTrack(currentTrackBeforeSwitching, upcomingTrackBeforeSwitching)
        }
    }

    private fun onPlayPauseClick() {
        if (!logic.player.isPlaying()) {
            play()
            Avo.play(logic.currentTrackName())
        } else {
            pause()
            Avo.pause(logic.currentTrackName())
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun play() {
        logic.player.play()
        findViewById<Button>(R.id.play_pause).text = getString(R.string.pause_label)

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                findViewById<View>(R.id.time_field).post {
                    showTrackTime()
                }
            }
        }, 0, 500)
    }

    private fun pause() {
        logic.player.pause()
        findViewById<Button>(R.id.play_pause).text = getString(R.string.play_label)

        timer?.cancel()
    }

    private fun showTrackTime() {
        findViewById<TextView>(R.id.time_field).text = getString(R.string.track_time,
                TimeUnit.SECONDS.convert(logic.player.currentPosition().toLong(), TimeUnit.MILLISECONDS),
                TimeUnit.SECONDS.convert(logic.player.trackLength().toLong(), TimeUnit.MILLISECONDS))
    }

    private fun manageNavigationButtonsVisibilityOnNext() {
        findViewById<View>(R.id.prev_track).visibility = View.VISIBLE
        if (!logic.hasNext()) {
            findViewById<View>(R.id.next_track).visibility = View.INVISIBLE
        }
    }

    private fun manageNavigationButtonsVisibilityOnPrev() {
        findViewById<View>(R.id.next_track).visibility = View.VISIBLE
        if (!logic.hasPrev()) {
            findViewById<View>(R.id.prev_track).visibility = View.INVISIBLE
        }
    }

    private fun showCurrentTrack() {
        findViewById<Button>(R.id.play_pause).text = getString(R.string.play_label)

        findViewById<TextView>(R.id.track_name).text = logic.currentTrackName()

        showTrackTime()
    }

    override fun onStop() {
        super.onStop()

        if (logic.player.isPlaying()) {
            pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logic.player.clear()
    }
}
