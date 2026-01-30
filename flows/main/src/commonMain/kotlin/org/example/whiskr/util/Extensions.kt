package org.example.whiskr.util

import org.example.whiskr.component.MainFlowComponent

fun MainFlowComponent.Child.toTab(): MainFlowComponent.Tab = when (this) {
    is MainFlowComponent.Child.Home -> MainFlowComponent.Tab.HOME
    is MainFlowComponent.Child.Explore -> MainFlowComponent.Tab.EXPLORE
    is MainFlowComponent.Child.AiStudio -> MainFlowComponent.Tab.AI_STUDIO
    is MainFlowComponent.Child.Games -> MainFlowComponent.Tab.GAMES
    is MainFlowComponent.Child.Messages -> MainFlowComponent.Tab.MESSAGES
    is MainFlowComponent.Child.Profile -> MainFlowComponent.Tab.PROFILE
    is MainFlowComponent.Child.Store -> MainFlowComponent.Tab.STORE
    else -> MainFlowComponent.Tab.HOME
}

fun MainFlowComponent.Tab.toConfig(): MainFlowComponent.Config = when (this) {
    MainFlowComponent.Tab.HOME -> MainFlowComponent.Config.Home
    MainFlowComponent.Tab.EXPLORE -> MainFlowComponent.Config.Explore
    MainFlowComponent.Tab.AI_STUDIO -> MainFlowComponent.Config.AiStudio
    MainFlowComponent.Tab.GAMES -> MainFlowComponent.Config.Games
    MainFlowComponent.Tab.MESSAGES -> MainFlowComponent.Config.Messages
    MainFlowComponent.Tab.PROFILE -> MainFlowComponent.Config.Profile
    MainFlowComponent.Tab.STORE -> MainFlowComponent.Config.Store
}

val MainFlowComponent.Child.showsNavigation: Boolean
    get() = when (this) {
        is MainFlowComponent.Child.CreatePost,
        is MainFlowComponent.Child.CreateReply,
        is MainFlowComponent.Child.PostDetails,
        is MainFlowComponent.Child.HashtagsFeed,
        is MainFlowComponent.Child.Profile,
        is MainFlowComponent.Child.MediaViewer -> false

        else -> true
    }