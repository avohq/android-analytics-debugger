package app.avo.musicplayerexample

import android.content.Context

class ExampleMusicPlayerLogic {

    val player = Player()

    private val musicStorage = MusicStorage()
    private var currentTrackPosition = 0

    fun loadPrevTrack(context: Context): Boolean {

        if (musicStorage.hasPrev(currentTrackPosition)) {
            currentTrackPosition -= 1
            loadCurrentTrack(context)

            return true
        }
        return false
    }

    fun loadNextTrack(context: Context): Boolean {

        if (musicStorage.hasNext(currentTrackPosition)) {
            currentTrackPosition += 1
            loadCurrentTrack(context)

            return true
        }
        return false
    }

    fun loadCurrentTrack(context: Context) {
        player.load(context, musicStorage.trackAsset(currentTrackPosition))
    }

    fun prevTrackName(): String {
        return musicStorage.trackName(currentTrackPosition - 1)
    }

    fun nextTrackName(): String {
        return musicStorage.trackName(currentTrackPosition + 1)
    }

    fun trackName(): String {
        return musicStorage.trackName(currentTrackPosition)
    }

    fun hasPrev(): Boolean {
        return musicStorage.hasPrev(currentTrackPosition)
    }

    fun hasNext(): Boolean {
        return musicStorage.hasNext(currentTrackPosition)
    }
}