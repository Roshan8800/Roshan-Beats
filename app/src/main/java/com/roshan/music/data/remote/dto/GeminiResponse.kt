package com.roshan.music.data.remote.dto

data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)
