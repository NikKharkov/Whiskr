package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.UserRepository
import domain.UserState
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.MainFlowComponent.Config.*
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.component.details.PostDetailsComponent
import org.example.whiskr.component.home.HomeComponent
import org.example.whiskr.component.reply.CreateReplyComponent
import org.example.whiskr.util.toConfig

@OptIn(DelicateDecomposeApi::class)
@Inject
class DefaultMainFlowComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted override val isDarkTheme: Value<Boolean>,
    private val userRepository: UserRepository,
    private val homeFactory: HomeComponent.Factory,
    private val createPostFactory: CreatePostComponent.Factory,
    private val createReplyFactory: CreateReplyComponent.Factory,
    private val postDetailsFactory: PostDetailsComponent.Factory,
    private val mediaViewerFactory: MediaViewerComponent.Factory
) : MainFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainFlowComponent.Config>()
    private val scope = componentScope()

    override val userState: Value<UserState> = userRepository.user

    private val _isDrawerOpen = MutableValue(false)
    override val isDrawerOpen: Value<Boolean> = _isDrawerOpen

    init {
        scope.launch {
            if (userRepository.user.value.profile == null) {
                userRepository.getMyProfile()
            }
        }
    }

    override fun setDrawerOpen(isOpen: Boolean) {
        _isDrawerOpen.value = isOpen
    }

    override val stack = childStack(
        source = navigation,
        serializer = MainFlowComponent.Config.serializer(),
        initialConfiguration = Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: MainFlowComponent.Config,
        context: ComponentContext
    ): MainFlowComponent.Child = when (config) {
        Home -> MainFlowComponent.Child.Home(
            homeFactory(
                componentContext = context,
                onNavigateToCreatePost = { navigation.push(CreatePost) },
                onNavigateToProfile = {},
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                },
                onNavigateToComments = { post ->
                    navigation.push(PostDetails(post))
                }
            )
        )

        CreatePost -> MainFlowComponent.Child.CreatePost(
            createPostFactory(
                componentContext = context,
                onBack = { navigation.pop() },
                onPostCreated = {
                    navigation.pop()
                }
            )
        )

        is CreateReply -> MainFlowComponent.Child.CreateReply(
            createReplyFactory(
                componentContext = context,
                targetPost = config.targetPost,
                onBack = { navigation.pop() },
                onReplyCreated = { navigation.pop() }
            )
        )

        is PostDetails -> MainFlowComponent.Child.PostDetails(
            postDetailsFactory(
                componentContext = context,
                post = config.post,
                onBack = { navigation.pop() },
                onNavigateToReply = { postToReply ->
                    navigation.push(CreateReply(targetPost = postToReply))
                },
                onNavigateToPostDetails = { post ->
                    navigation.push(PostDetails(post))
                },
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                }
            )
        )

        is MediaViewer -> MainFlowComponent.Child.MediaViewer(
            mediaViewerFactory(
                componentContext = context,
                mediaList = config.media,
                initialIndex = config.index,
                onFinished = { navigation.pop() }
            )
        )

        is UserProfile -> TODO()
        AIStudio -> TODO()
        Explore -> TODO()
        Games -> TODO()
        Messages -> TODO()
        Profile -> TODO()
    }

    override fun onTabSelected(tab: MainFlowComponent.Tab) {
        navigation.bringToFront(tab.toConfig())
    }

    override fun onPostClick() = navigation.push(CreatePost)
}