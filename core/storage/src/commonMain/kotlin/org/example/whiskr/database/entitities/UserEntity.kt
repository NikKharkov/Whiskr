package org.example.whiskr.database.entitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val userId: Long,
    val id: Long,
    val handle: String,
    val displayName: String?,
    val bio: String?,
    val avatarUrl: String?,
    val followersCount: Int
)
