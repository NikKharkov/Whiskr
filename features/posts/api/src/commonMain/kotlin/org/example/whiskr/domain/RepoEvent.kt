package org.example.whiskr.domain

import org.example.whiskr.data.Post

sealed interface RepoEvent {
    data class NewPost(val post: Post) : RepoEvent
    data class PostUpdated(val post: Post) : RepoEvent
}