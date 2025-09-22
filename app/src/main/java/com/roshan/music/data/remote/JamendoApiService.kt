package com.roshan.music.data.remote

import com.roshan.music.data.remote.dto.JamendoAlbumResponse
import com.roshan.music.data.remote.dto.JamendoArtistResponse
import com.roshan.music.data.remote.dto.JamendoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApiService {

    @GET("tracks")
    suspend fun searchTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("search") searchQuery: String
    ): JamendoResponse

    @GET("albums")
    suspend fun getAlbums(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("order") order: String,
        @Query("tags") tags: String? = null
    ): JamendoAlbumResponse

    @GET("artists")
    suspend fun searchArtists(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("namesearch") searchQuery: String
    ): JamendoArtistResponse

    @GET("albums")
    suspend fun searchAlbums(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("namesearch") searchQuery: String
    ): JamendoAlbumResponse
}
