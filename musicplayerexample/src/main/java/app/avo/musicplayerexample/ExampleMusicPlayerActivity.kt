package app.avo.musicplayerexample


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import app.avo.androidanalyticsdebugger.DebuggerManager
import app.avo.androidanalyticsdebugger.DebuggerMode
import app.avo.androidanalyticsdebugger.EventProperty
import app.avo.androidanalyticsdebugger.PropertyError
import kotlinx.android.synthetic.main.activity_music_player.*
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

        play_pause.setOnClickListener {
            onPlayPauseClick()
        }
        next_track.setOnClickListener {
            onNextTrackClick()
        }
        prev_track.setOnClickListener {
            onPrevTrackClick()
        }

        debuggerManager.publishEvent(/*timestamp =*/ System.currentTimeMillis(), /*name =*/ "Test custom event",
                /*properties =*/ listOf(EventProperty(/*id =*/ "id0", /*name =*/ "property0", "value"),
                    EventProperty(/*id =*/ "id1", /*name =*/ "property1", "value")),
                /*errors =*/ listOf(PropertyError(/*propertyId =*/ "id0", /*message =*/ "Error in property with id 'id0'")))
    }

    private fun initShowDebuggerButtons() {
        show_bubble_debugger.setOnClickListener {
            debuggerManager.showDebugger(this, DebuggerMode.bubble)
        }
        show_bar_debugger.setOnClickListener {
            debuggerManager.showDebugger(this, DebuggerMode.bar)
        }
        hide_debugger.setOnClickListener {
            debuggerManager.hideDebugger(this)
        }
    }

    private fun initialState() {
        logic.loadCurrentTrack(applicationContext)
        showCurrentTrack()
        prev_track.visibility = View.INVISIBLE
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

    private fun play() {
        logic.player.play()
        play_pause.text = getString(R.string.pause_label)

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time_field.post {
                    showTrackTime()
                }
            }
        }, 0, 500)
    }

    private fun pause() {
        logic.player.pause()
        play_pause.text = getString(R.string.play_label)

        timer?.cancel()
    }

    private fun showTrackTime() {
        time_field.text = getString(R.string.track_time,
                TimeUnit.SECONDS.convert(logic.player.currentPosition().toLong(), TimeUnit.MILLISECONDS),
                TimeUnit.SECONDS.convert(logic.player.trackLength().toLong(), TimeUnit.MILLISECONDS))
    }

    private fun manageNavigationButtonsVisibilityOnNext() {
        prev_track.visibility = View.VISIBLE
        if (!logic.hasNext()) {
            next_track.visibility = View.INVISIBLE
        }
    }

    private fun manageNavigationButtonsVisibilityOnPrev() {
        next_track.visibility = View.VISIBLE
        if (!logic.hasPrev()) {
            prev_track.visibility = View.INVISIBLE
        }
    }

    private fun showCurrentTrack() {
        play_pause.text = getString(R.string.play_label)

        track_name.text = logic.currentTrackName()

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
