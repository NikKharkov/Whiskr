package org.example.whiskr.domain

import org.example.whiskr.dto.Post

sealed interface RepoEvent {
    data class NewPost(val post: Post) : RepoEvent
    data class PostUpdated(val post: Post) : RepoEvent
}