package com.dev.gauravmisra.the_offencive_joker.audio



import android.content.Context
import android.media.MediaPlayer

fun playSwipeSound(context: Context, assetFileName: String = "swipe_up.mp3") {
    val afd = context.assets.openFd(assetFileName)
    MediaPlayer().apply {
        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        prepare()
        start()
        setOnCompletionListener { release() }
    }
}
