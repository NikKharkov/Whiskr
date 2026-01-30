package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import component.ProfileComponent
import domain.UserRepository
import domain.UserState
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.MainFlowComponent.Config.AiStudio
import org.example.whiskr.component.MainFlowComponent.Config.CreatePost
import org.example.whiskr.component.MainFlowComponent.Config.CreateReply
import org.example.whiskr.component.MainFlowComponent.Config.Explore
import org.example.whiskr.component.MainFlowComponent.Config.Games
import org.example.whiskr.component.MainFlowComponent.Config.Home
import org.example.whiskr.component.MainFlowComponent.Config.MediaViewer
import org.example.whiskr.component.MainFlowComponent.Config.Messages
import org.example.whiskr.component.MainFlowComponent.Config.PostDetails
import org.example.whiskr.component.MainFlowComponent.Config.Profile
import org.example.whiskr.component.MainFlowComponent.Config.UserProfile
import org.example.whiskr.domain.BillingRepository
import org.example.whiskr.data.WalletResponseDto
import org.example.whiskr.util.toAiPostMedia
import org.example.whiskr.util.toConfig

@OptIn(DelicateDecomposeApi::class)
@Inject
class DefaultMainFlowComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted override val isDarkTheme: Value<Boolean>,
    @Assisted private val deepLink: String?,
    private val userRepository: UserRepository,
    private val billingRepository: BillingRepository,
    private val homeFactory: HomeComponent.Factory,
    private val createPostFactory: CreatePostComponent.Factory,
    private val createReplyFactory: CreateReplyComponent.Factory,
    private val postDetailsFactory: PostDetailsComponent.Factory,
    private val mediaViewerFactory: MediaViewerComponent.Factory,
    private val hashtagsFactory: HashtagsComponent.Factory,
    private val storeFactory: StoreComponent.Factory,
    private val aiFactory: AiStudioComponent.Factory,
    private val profileFactory: ProfileComponent.Factory,
    private val repostFactory: CreateRepostComponent.Factory
) : MainFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainFlowComponent.Config>()
    private val dialogNavigation = SlotNavigation<MainFlowComponent.DialogConfig>()

    private val scope = componentScope()

    override val userState: Value<UserState> = userRepository.user
    override val walletState: Value<WalletResponseDto> = billingRepository.wallet

    private val _isDrawerOpen = MutableValue(false)
    override val isDrawerOpen: Value<Boolean> = _isDrawerOpen

    init {
        scope.launch {
            billingRepository.getWallet()
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
        initialStack = { getInitialStack(deepLink) },
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val dialogSlot = childSlot(
        source = dialogNavigation,
        serializer = MainFlowComponent.DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialogChild
    )

    private fun createChild(
        config: MainFlowComponent.Config,
        context: ComponentContext
    ): MainFlowComponent.Child = when (config) {
        Home -> MainFlowComponent.Child.Home(
            homeFactory(
                componentContext = context,
                onNavigateToCreatePost = {
                    navigation.push(CreatePost())
                },
                onNavigateToProfile = { userHandle ->
                    navigation.bringToFront(UserProfile(userHandle))
                },
                onNavigateToComments = { post ->
                    navigation.push(PostDetails(postId = post.id))
                },
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(MainFlowComponent.Config.HashtagsFeed(tag))
                },
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            )
        )

        is PostDetails -> MainFlowComponent.Child.PostDetails(
            postDetailsFactory(
                componentContext = context,
                postId = config.postId,
                onBack = { navigation.pop() },
                onNavigateToReply = { postToReply ->
                    navigation.push(CreateReply(targetPost = postToReply))
                },
                onNavigateToPostDetails = { post ->
                    navigation.push(PostDetails(postId = post.id))
                },
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(MainFlowComponent.Config.HashtagsFeed(tag))
                },
                onNavigateToProfile = { handle ->
                    navigation.bringToFront(UserProfile(handle))
                },
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            )
        )

        is MainFlowComponent.Config.HashtagsFeed -> MainFlowComponent.Child.HashtagsFeed(
            hashtagsFactory(
                componentContext = context,
                hashtag = config.hashtag,
                onBack = { navigation.pop() },
                onNavigateToComments = { post ->
                    navigation.push(PostDetails(postId = post.id))
                },
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(MainFlowComponent.Config.HashtagsFeed(tag))
                },
                onNavigateToProfile = { handle ->
                    navigation.bringToFront(UserProfile(handle))
                },
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            )
        )

        is UserProfile -> MainFlowComponent.Child.Profile(
            profileFactory(
                componentContext = context,
                handle = config.handle,
                onBack = { navigation.pop() },
                onNavigateToPost = { post -> navigation.push(PostDetails(postId = post.id)) },
                onNavigateToUserProfile = { handle -> navigation.bringToFront(UserProfile(handle)) },
                onNavigateToMediaViewer = { media, index ->
                    navigation.push(
                        MediaViewer(
                            media,
                            index
                        )
                    )
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(
                        MainFlowComponent.Config.HashtagsFeed(
                            tag
                        )
                    )
                },
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            ),
            isMe = false
        )

        Profile -> MainFlowComponent.Child.Profile(
            profileFactory(
                componentContext = context,
                handle = userState.value.profile?.handle ?: "",
                onBack = { navigation.pop() },
                onNavigateToPost = { post -> navigation.push(PostDetails(postId = post.id)) },
                onNavigateToUserProfile = { handle -> navigation.bringToFront(UserProfile(handle)) },
                onNavigateToMediaViewer = { media, index ->
                    navigation.push(
                        MediaViewer(
                            media,
                            index
                        )
                    )
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(
                        MainFlowComponent.Config.HashtagsFeed(
                            tag
                        )
                    )
                },
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            ),
            isMe = true
        )

        is CreatePost -> MainFlowComponent.Child.CreatePost(
            createPostFactory(
                componentContext = context,
                initialImageUrl = config.imageUrl,
                onBack = { navigation.pop() },
                onPostCreated = { navigation.pop() }
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

        is MediaViewer -> MainFlowComponent.Child.MediaViewer(
            mediaViewerFactory(
                componentContext = context,
                mediaList = config.media,
                initialIndex = config.index,
                onFinished = { navigation.pop() }
            )
        )

        MainFlowComponent.Config.Store -> MainFlowComponent.Child.Store(
            storeFactory(
                componentContext = context,
                onBack = { navigation.pop() }
            )
        )

        AiStudio -> MainFlowComponent.Child.AiStudio(
            aiFactory(
                componentContext = context,
                initialBalance = walletState.value.balance,
                onNavigateToMediaViewer = { url ->
                    navigation.push(
                        MediaViewer(
                            media = listOf(url.toAiPostMedia()),
                            index = 0
                        )
                    )
                },
                onNavigateToCreatePost = { url ->
                    navigation.push(CreatePost(imageUrl = url))
                }
            )
        )

        Explore -> TODO()
        Games -> TODO()
        Messages -> TODO()
    }

    private fun createDialogChild(
        config: MainFlowComponent.DialogConfig,
        context: ComponentContext
    ): MainFlowComponent.DialogChild = when (config) {
        is MainFlowComponent.DialogConfig.CreateRepost -> MainFlowComponent.DialogChild.CreateRepost(
            repostFactory(
                componentContext = context,
                targetPost = config.targetPost,
                onBack = { dialogNavigation.dismiss() },
                onRepostCreated = { dialogNavigation.dismiss() }
            )
        )
    }

    override fun onDeepLink(link: String) {
        val newStack = getInitialStack(link)
        if (newStack.size > 1) {
            val destination = newStack.last()
            navigation.push(destination)
        }
    }

    private fun getInitialStack(link: String?): List<MainFlowComponent.Config> {
        if (link == null) return listOf(Home)

        if (link.contains("post")) {
            val id = link.substringAfterLast("/").toLongOrNull()

            if (id != null) {
                return listOf(
                    Home,
                    PostDetails(postId = id)
                )
            }
        }
        return listOf(Home)
    }

    override fun onTabSelected(tab: MainFlowComponent.Tab) {
        navigation.bringToFront(tab.toConfig())
    }

    override fun onPostClick() = navigation.push(CreatePost())
}