package com.roshan.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Song(
    val title: String,
    val artist: String,
    val albumArtUrl: String
)

data class NowPlayingUiState(
    val currentSong: Song?,
    val isPlaying: Boolean,
    val progress: Float
)

class NowPlayingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        NowPlayingUiState(
            currentSong = Song(
                title = "Blinding Lights",
                artist = "The Weeknd",
                albumArtUrl = "https://upload.wikimedia.org/wikipedia/en/e/e6/The_Weeknd_-_Blinding_Lights.png"
            ),
            isPlaying = true,
            progress = 0.3f
        )
    )
    val uiState: StateFlow<NowPlayingUiState> = _uiState
}
