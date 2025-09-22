package com.roshan.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Playlist(
    val id: String,
    val name: String,
    val coverArtUrl: String,
    val trackCount: Int
)

data class LibraryUiState(
    val playlists: List<Playlist> = emptyList(),
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val downloads: List<Song> = emptyList()
)

data class Album(val id: String, val name: String, val artist: String, val coverArtUrl: String)
data class Artist(val id: String, val name: String, val avatarUrl: String)


class LibraryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        LibraryUiState(
            playlists = listOf(
                Playlist("1", "Chill Mix", "", 23),
                Playlist("2", "Workout", "", 50),
                Playlist("3", "Road Trip", "", 100)
            )
        )
    )
    val uiState: StateFlow<LibraryUiState> = _uiState
}
