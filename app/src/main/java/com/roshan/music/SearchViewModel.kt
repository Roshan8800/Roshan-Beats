package com.roshan.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SearchUiState(
    val searchQuery: String = "",
    val trendingSearches: List<String> = emptyList(),
    val recentSearches: List<String> = emptyList()
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchUiState(
            trendingSearches = listOf("Top Hits", "New Releases", "Podcasts"),
            recentSearches = listOf("The Weeknd", "Chill Mix")
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onSearchQueryChanged(query: String) {
        // TODO: implement search logic
    }
}
