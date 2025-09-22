package com.roshan.music.data.remote

import com.roshan.music.data.remote.dto.OpenRouterRequest
import com.roshan.music.data.remote.dto.OpenRouterResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenRouterApiService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletions(
        @Header("Authorization") apiKey: String,
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}
