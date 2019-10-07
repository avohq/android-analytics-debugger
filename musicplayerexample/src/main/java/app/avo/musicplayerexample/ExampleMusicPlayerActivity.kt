package app.avo.musicplayerexample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import app.avo.androidanalyticsdebugger.Debugger
import app.avo.androidanalyticsdebugger.DebuggerMode
import kotlinx.android.synthetic.main.activity_music_player.*
import java.util.*
import java.util.concurrent.TimeUnit

class ExampleMusicPlayerActivity : AppCompatActivity() {

    private val logic = ExampleMusicPlayerLogic()

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_music_player)

        initialState()

        initDebugger()

        play_pause.setOnClickListener {
            onPlayPauseClick()
        }
        next_track.setOnClickListener {
            onNextTrackClick()
        }
        prev_track.setOnClickListener {
            onPrevTrackClick()
        }
    }

    private fun initDebugger() {
        val debugger = Debugger()

        show_bubble_debugger.setOnClickListener {
            debugger.showDebugger(this, DebuggerMode.bubble)
        }
        show_bar_debugger.setOnClickListener {
            debugger.showDebugger(this, DebuggerMode.bar)
        }
        hide_debugger.setOnClickListener {
            debugger.hideDebugger(this)
        }
    }

    private fun initialState() {
        logic.loadCurrentTrack(applicationContext)
        showCurrentTrack()
        prev_track.visibility = View.INVISIBLE
    }

    private fun onPrevTrackClick() {
        if (logic.loadPrevTrack(applicationContext)) {
            showCurrentTrack()
            manageNavigationButtonsVisibilityOnPrev()
        }
    }

    private fun onNextTrackClick() {
        if (logic.loadNextTrack(applicationContext)) {
            showCurrentTrack()
            manageNavigationButtonsVisibilityOnNext()
        }
    }

    private fun onPlayPauseClick() {
        if (!logic.player.isPlaying()) {
            play()
        } else {
            pause()
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

        track_name.text = logic.trackName()

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