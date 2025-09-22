package com.roshan.music.data.remote.dto

data class TrackDto(
    val id: String,
    val name: String,
    val duration: Int,
    val artist_id: String,
    val artist_name: String,
    val artist_idstr: String,
    val album_name: String,
    val album_id: String,
    val license_ccurl: String,
    val position: Int,
    val releasedate: String,
    val album_image: String,
    val image: String,
    val audio: String,
    val audiodownload: String,
    val audiodownload_allowed: Boolean,
    val prourl: String,
    val shorturl: String,
    val shareurl: String,
    val waveform: String,
    val license_parent: String
)
