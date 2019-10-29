package app.avo.musicplayerexample

import android.content.Context
import android.media.MediaPlayer
import android.support.annotation.RawRes


class Player {
    private var mediaPlayer = MediaPlayer()

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun load(context: Context, @RawRes trackResourceId: Int) =
        context.resources.openRawResourceFd(trackResourceId).use { trackResource ->
            mediaPlayer.reset()
            mediaPlayer.setDataSource(trackResource.fileDescriptor, trackResource.startOffset,
                    trackResource.length)
            mediaPlayer.prepare()
        }

    fun play() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun trackLength(): Int {
        return mediaPlayer.duration
    }

    fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun clear() {
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}