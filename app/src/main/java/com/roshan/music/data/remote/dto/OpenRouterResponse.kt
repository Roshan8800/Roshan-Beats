package com.roshan.music.data.remote.dto

data class OpenRouterResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
