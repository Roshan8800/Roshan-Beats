package com.roshan.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.roshan.music.data.database.AppDatabase
import com.roshan.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val trendingAlbums: List<com.roshan.music.data.remote.dto.AlbumDto> = emptyList(),
    val genreAlbums: Map<String, List<com.roshan.music.data.remote.dto.AlbumDto>> = emptyMap(),
    val aiRecommendations: List<com.roshan.music.data.remote.dto.TrackDto> = emptyList(),
    val showAiDialog: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val repository: MusicRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MusicRepository(database)
        fetchTrendingAlbums()
        fetchAlbumsByGenre("rock")
    }

    private fun fetchTrendingAlbums() {
        viewModelScope.launch {
            val albums = repository.getTrendingAlbums()
            _uiState.value = _uiState.value.copy(trendingAlbums = albums)
        }
    }

    private fun fetchAlbumsByGenre(genre: String) {
        viewModelScope.launch {
            val albums = repository.getAlbumsByGenre(genre)
            val currentGenres = _uiState.value.genreAlbums.toMutableMap()
            currentGenres[genre] = albums
            _uiState.value = _uiState.value.copy(genreAlbums = currentGenres)
        }
    }

    fun showAiDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAiDialog = show)
    }

    fun getAiRecommendations(prompt: String) {
        viewModelScope.launch {
            val recommendations = repository.getSongRecommendations(prompt)
            val songs = recommendations.split(",").map { it.trim() }
            val tracks = mutableListOf<com.roshan.music.data.remote.dto.TrackDto>()
            for (song in songs) {
                val results = repository.searchTracks(song)
                if (results.isNotEmpty()) {
                    tracks.add(results.first())
                }
            }
            _uiState.value = _uiState.value.copy(aiRecommendations = tracks)
        }
    }
}
