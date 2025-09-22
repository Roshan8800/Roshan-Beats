package com.roshan.music.data.remote

import com.roshan.music.data.remote.dto.GeminiRequest
import com.roshan.music.data.remote.dto.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Body request: GeminiRequest,
        @Query("key") apiKey: String
    ): GeminiResponse
}
