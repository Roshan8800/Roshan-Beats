package com.roshan.music

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.roshan.music.data.database.AppDatabase
import com.roshan.music.data.remote.dto.AlbumDto
import com.roshan.music.data.remote.dto.ArtistDto
import com.roshan.music.data.remote.dto.TrackDto
import com.roshan.music.data.repository.MusicRepository
import com.roshan.music.media.MusicControllerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val tracks: List<TrackDto> = emptyList(),
    val albums: List<AlbumDto> = emptyList(),
    val artists: List<ArtistDto> = emptyList()
)

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState
    private val repository: MusicRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MusicRepository(database)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun search() {
        viewModelScope.launch {
            val query = _uiState.value.searchQuery
            val tracks = repository.searchTracks(query)
            val albums = repository.searchAlbums(query)
            val artists = repository.searchArtists(query)
            _uiState.value = _uiState.value.copy(
                tracks = tracks,
                albums = albums,
                artists = artists
            )
        }
    }

}
