package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.UserState
import kotlinx.serialization.Serializable
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.component.home.HomeComponent
import org.example.whiskr.dto.PostMedia
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.ic_ai
import whiskr.flows.main.generated.resources.ic_explore
import whiskr.flows.main.generated.resources.ic_games
import whiskr.flows.main.generated.resources.ic_home
import whiskr.flows.main.generated.resources.ic_messages
import whiskr.flows.main.generated.resources.ic_profile
import whiskr.flows.main.generated.resources.tab_ai
import whiskr.flows.main.generated.resources.tab_explore
import whiskr.flows.main.generated.resources.tab_games
import whiskr.flows.main.generated.resources.tab_home
import whiskr.flows.main.generated.resources.tab_messages
import whiskr.flows.main.generated.resources.tab_profile

interface MainFlowComponent {

    val stack: Value<ChildStack<Config, Child>>
    val userState: Value<UserState>
    val isDarkTheme: Value<Boolean>
    val isDrawerOpen: Value<Boolean>

    fun setDrawerOpen(isOpen: Boolean)
    fun onTabSelected(tab: Tab)

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class CreatePost(val component: CreatePostComponent) : Child()
        class MediaViewer(val component: MediaViewerComponent) : Child()
        class Explore(val component: Any) : Child()
        class AIStudio(val component: Any) : Child()
        class Games(val component: Any) : Child()
        class Messages(val component: Any) : Child()
        class Profile(val component: Any) : Child()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Explore : Config

        @Serializable
        data object AIStudio : Config

        @Serializable
        data object Games : Config

        @Serializable
        data object Messages : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object CreatePost : Config

        @Serializable
        data class UserProfile(val userId: Long) : Config

        @Serializable
        data class MediaViewer(
            val media: List<PostMedia>,
            val index: Int
        ) : Config
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            isDarkTheme: Value<Boolean>
        ): MainFlowComponent
    }

    enum class Tab(
        val label: StringResource,
        val icon: DrawableResource,
        val showInBottomBar: Boolean = true
    ) {
        HOME(Res.string.tab_home, Res.drawable.ic_home),
        EXPLORE(Res.string.tab_explore, Res.drawable.ic_explore),
        AI_STUDIO(Res.string.tab_ai, Res.drawable.ic_ai),
        GAMES(Res.string.tab_games, Res.drawable.ic_games),
        PROFILE(Res.string.tab_profile, Res.drawable.ic_profile, false),
        MESSAGES(Res.string.tab_messages, Res.drawable.ic_messages)
    }
}