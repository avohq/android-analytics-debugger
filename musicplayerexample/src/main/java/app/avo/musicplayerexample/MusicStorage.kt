package app.avo.musicplayerexample

class MusicStorage {

    private val tracks = listOf("greek_loop_mix.mp3", "jingle_jungle_around_the_bot.wav", "cartoon-whistle.wav")

    fun hasNext(fromPosition: Int): Boolean {
        return this.tracks.size - 1 > fromPosition
    }

    fun hasPrev(fromPosition: Int): Boolean {
        return fromPosition > 0
    }

    fun trackName(position: Int): String {
        return this.trackFile(position).split(".")[0]
    }

    fun trackFile(position: Int): String {
        return this.tracks[position]
    }

    fun trackAsset(position: Int): Int {
        return when(position) {
            0 -> R.raw.greek_loop_mix
            1 -> R.raw.jingle_jungle_around_the_bot
            2 -> R.raw.cartoon_whistle
            else -> -1
        }
    }
}