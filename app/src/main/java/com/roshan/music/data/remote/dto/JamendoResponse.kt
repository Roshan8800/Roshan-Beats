package com.roshan.music.data.remote.dto

data class JamendoResponse(
    val headers: Headers,
    val results: List<TrackDto>
)

data class Headers(
    val status: String,
    val code: Int,
    val error_message: String,
    val warnings: String,
    val results_count: Int
)
