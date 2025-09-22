package com.roshan.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class User(
    val username: String,
    val avatarUrl: String,
    val subscriptionTier: String
)

data class ProfileUiState(
    val user: User? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            user = User(
                username = "Roshan",
                avatarUrl = "",
                subscriptionTier = "Premium"
            )
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState
}
