package org.example.whiskr.util

import org.example.whiskr.component.MainFlowComponent

fun MainFlowComponent.Child.toTab(): MainFlowComponent.Tab? = when (this) {
    is MainFlowComponent.Child.Home -> MainFlowComponent.Tab.HOME
    is MainFlowComponent.Child.Explore -> MainFlowComponent.Tab.EXPLORE
    is MainFlowComponent.Child.AiStudio -> MainFlowComponent.Tab.AI_STUDIO
    is MainFlowComponent.Child.Games -> MainFlowComponent.Tab.GAMES
    is MainFlowComponent.Child.Messages -> MainFlowComponent.Tab.MESSAGES
    is MainFlowComponent.Child.Store -> MainFlowComponent.Tab.STORE
    is MainFlowComponent.Child.Notifications -> MainFlowComponent.Tab.NOTIFICATIONS
    is MainFlowComponent.Child.Profile -> {
        if (isMe) {
            MainFlowComponent.Tab.PROFILE
        } else {
            null
        }
    }
    else -> null
}

fun MainFlowComponent.Tab.toConfig(): MainFlowComponent.Config = when (this) {
    MainFlowComponent.Tab.HOME -> MainFlowComponent.Config.Home
    MainFlowComponent.Tab.EXPLORE -> MainFlowComponent.Config.Explore
    MainFlowComponent.Tab.AI_STUDIO -> MainFlowComponent.Config.AiStudio
    MainFlowComponent.Tab.GAMES -> MainFlowComponent.Config.Games
    MainFlowComponent.Tab.MESSAGES -> MainFlowComponent.Config.Messages
    MainFlowComponent.Tab.PROFILE -> MainFlowComponent.Config.Profile
    MainFlowComponent.Tab.STORE -> MainFlowComponent.Config.Store
    MainFlowComponent.Tab.NOTIFICATIONS -> MainFlowComponent.Config.Notifications
}

val MainFlowComponent.Child.showsNavigation: Boolean
    get() = when (this) {
        is MainFlowComponent.Child.CreatePost,
        is MainFlowComponent.Child.CreateReply,
        is MainFlowComponent.Child.PostDetails,
        is MainFlowComponent.Child.HashtagsFeed,
        is MainFlowComponent.Child.Profile,
        is MainFlowComponent.Child.Notifications,
        is MainFlowComponent.Child.MediaViewer -> false

        else -> true
    }

fun String.toMainFlowConfig(): MainFlowComponent.Config? {
    val path = this.substringBefore("?")
        .replace("https://whiskr.app", "")
        .replace("whiskr://", "")
        .trimStart('/')

    return when {
        path.startsWith("post/") -> {
            val id = path.removePrefix("post/")
                .replace("/", "")
                .toLongOrNull()

            id?.let { MainFlowComponent.Config.PostDetails(it) }
        }

        path.startsWith("profile/") -> {
            val handle = path.removePrefix("profile/")
                .replace("/", "")
                .replace("%40", "@")
                .trim()

            if (handle.isBlank()) return null

            MainFlowComponent.Config.UserProfile(handle)
        }

        else -> null
    }
}