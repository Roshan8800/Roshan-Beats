package com.roshan.music.data.remote.dto

data class AlbumDto(
    val id: String,
    val name: String,
    val releasedate: String,
    val artist_id: String,
    val artist_name: String,
    val image: String,
    val zip: String,
    val shorturl: String,
    val shareurl: String,
    val zip_allowed: Boolean
)
