package com.roshan.music.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.roshan.music.MusicService

object MusicControllerHelper {

    var mediaControllerFuture: ListenableFuture<MediaController>? = null

    fun getController(context: Context): ListenableFuture<MediaController> {
        if (mediaControllerFuture == null) {
            val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
            mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        }
        return mediaControllerFuture!!
    }

    fun releaseController() {
        mediaControllerFuture?.let {
            MediaController.releaseFuture(it)
            mediaControllerFuture = null
        }
    }
}
