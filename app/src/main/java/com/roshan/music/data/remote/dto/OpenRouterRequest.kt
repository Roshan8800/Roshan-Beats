package com.roshan.music.data.remote.dto

data class OpenRouterRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)
