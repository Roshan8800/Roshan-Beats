package com.roshan.music

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.roshan.music.media.MusicControllerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class NowPlayingUiState(
    val currentTrack: MediaItem?,
    val isPlaying: Boolean,
    val progress: Long,
    val duration: Long
)

class NowPlayingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingUiState(null, false, 0, 0))
    val uiState: StateFlow<NowPlayingUiState> = _uiState

    fun init(context: Context) {
        val controllerFuture = MusicControllerHelper.getController(context)
        controllerFuture.addListener({
            val controller = controllerFuture.get()
            controller.addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    _uiState.value = _uiState.value.copy(currentTrack = mediaItem)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
                }
            })
        }, androidx.core.content.ContextCompat.getMainExecutor(context))
    }

    fun playTrack(track: com.roshan.music.data.remote.dto.TrackDto) {
        MusicControllerHelper.mediaControllerFuture?.get()?.let { controller ->
            val mediaItem = MediaItem.fromUri(track.audio)
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
        }
    }

    fun play() {
        MusicControllerHelper.mediaControllerFuture?.get()?.play()
    }

    fun pause() {
        MusicControllerHelper.mediaControllerFuture?.get()?.pause()
    }

    fun skipToNext() {
        MusicControllerHelper.mediaControllerFuture?.get()?.seekToNext()
    }

    fun skipToPrevious() {
        MusicControllerHelper.mediaControllerFuture?.get()?.seekToPrevious()
    }
}
