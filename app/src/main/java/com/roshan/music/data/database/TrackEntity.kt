package com.roshan.music.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val artistName: String,
    val albumName: String,
    val imageUrl: String,
    val audioUrl: String,
    val isDownloaded: Boolean = false,
    val localPath: String? = null
)

@Entity(tableName = "playlist_track_cross_ref", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: String
)
