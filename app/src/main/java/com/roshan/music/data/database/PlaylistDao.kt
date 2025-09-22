package com.roshan.music.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class PlaylistWithTracks(
    val playlist: PlaylistEntity,
    @androidx.room.Relation(
        parentColumn = "id",
        entity = TrackEntity::class,
        associateBy = androidx.room.Junction(PlaylistTrackCrossRef::class)
    )
    val tracks: List<TrackEntity>
)

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert
    suspend fun insertTrackIntoPlaylist(crossRef: PlaylistTrackCrossRef)

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>
}
