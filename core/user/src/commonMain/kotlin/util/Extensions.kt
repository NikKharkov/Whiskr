package util

import data.UserResponseDto
import org.example.whiskr.database.entitities.UserEntity

fun UserResponseDto.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        id = this.id,
        handle = this.handle,
        displayName = this.displayName,
        bio = this.bio,
        avatarUrl = this.avatarUrl,
        followersCount = this.followersCount
    )
}

fun UserEntity.toDto(): UserResponseDto {
    return UserResponseDto(
        userId = this.userId,
        id = this.id,
        handle = this.handle,
        displayName = this.displayName,
        bio = this.bio,
        avatarUrl = this.avatarUrl,
        followersCount = this.followersCount
    )
}