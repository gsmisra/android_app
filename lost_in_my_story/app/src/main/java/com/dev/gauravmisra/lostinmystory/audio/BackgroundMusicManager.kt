package com.dev.gauravmisra.lostinmystory.audio


import android.content.Context
import android.media.MediaPlayer
import com.dev.gauravmisra.lostinmystory.R

object BackgroundMusicManager {

    private var mediaPlayer: MediaPlayer? = null

    fun start(context: Context) {
        // Already playing → do nothing
        if (mediaPlayer?.isPlaying == true) return

        // Avoid chaining .apply{} on MediaPlayer.create(...) to prevent type inference issues
        val mp: MediaPlayer = MediaPlayer.create(
            context.applicationContext,
            R.raw.dark_ominous_loop
        ) ?: return

        // Use the reliable Java API call instead of isLooping property
        mp.setLooping(true) // [1](https://stackoverflow.com/questions/9461270/media-player-looping-android)[2](https://developer.android.com/reference/android/media/MediaPlayer)

        // Dark ambient should be subtle
        mp.setVolume(0.35f, 0.35f)

        mp.start()
        mediaPlayer = mp
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun resume() {
        if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun stop() {
        mediaPlayer?.run {
            try {
                stop()
            } catch (_: Exception) {
                // stop() can throw if called in a wrong state; ignore safely
            }
            release()
        }
        mediaPlayer = null
    }
}
