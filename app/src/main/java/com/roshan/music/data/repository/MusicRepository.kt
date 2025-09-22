package com.roshan.music.data.repository

import com.roshan.music.BuildConfig
import com.roshan.music.data.remote.GeminiApiService
import com.roshan.music.data.remote.OpenRouterApiService
import com.roshan.music.data.remote.RetrofitClient
import com.roshan.music.data.remote.dto.Content
import com.roshan.music.data.remote.dto.GeminiRequest
import com.roshan.music.data.remote.dto.Message
import com.roshan.music.data.remote.dto.OpenRouterRequest
import com.roshan.music.data.remote.dto.Part

class MusicRepository {

    private val geminiApiService: GeminiApiService = RetrofitClient.geminiApiService
    private val openRouterApiService: OpenRouterApiService = RetrofitClient.openRouterApiService

    suspend fun getSongRecommendations(prompt: String): String {
        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )
        val response = geminiApiService.generateContent(request, BuildConfig.GEMINI_API_KEY)
        return response.candidates.first().content.parts.first().text
    }

    suspend fun getChatResponse(prompt: String): String {
        val request = OpenRouterRequest(
            model = "deepseek/deepseek-v3.1",
            messages = listOf(
                Message(role = "user", content = prompt)
            )
        )
        val response = openRouterApiService.getChatCompletions("Bearer ${BuildConfig.DEEPSEEK_V3_1_API_KEY}", request)
        return response.choices.first().message.content
    }
}
