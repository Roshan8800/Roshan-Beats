package com.roshan.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.roshan.music.data.database.AppDatabase
import com.roshan.music.data.database.PlaylistEntity
import com.roshan.music.data.database.PlaylistWithTracks
import com.roshan.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class LibraryUiState(
    val playlists: List<PlaylistWithTracks> = emptyList(),
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val downloads: List<Song> = emptyList()
)

data class Album(val id: String, val name: String, val artist: String, val coverArtUrl: String)
data class Artist(val id: String, val name: String, val avatarUrl: String)


class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState
    private val repository: MusicRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MusicRepository(database)
        viewModelScope.launch {
            repository.getPlaylists().collect { playlists ->
                _uiState.value = _uiState.value.copy(playlists = playlists)
            }
        }
    }

    fun createPlaylist(name: String, description: String?) {
        viewModelScope.launch {
            repository.createPlaylist(PlaylistEntity(name = name, description = description, coverImagePath = null))
        }
    }
}
