package org.example.whiskr.component

import co.touchlab.kermit.Logger
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
import component.add_pet.AddPetComponent
import component.edit_pet.EditPetComponent
import component.edit_profile.EditProfileComponent
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
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.component.viewer.NewsViewerComponent
import org.example.whiskr.data.WalletResponseDto
import org.example.whiskr.domain.BillingRepository
import org.example.whiskr.domain.NotificationRepository
import org.example.whiskr.util.toAiPostMedia
import org.example.whiskr.util.toConfig
import org.example.whiskr.util.toMainFlowConfig

@OptIn(DelicateDecomposeApi::class)
@Inject
class DefaultMainFlowComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted override val isDarkTheme: Value<Boolean>,
    @Assisted private val deepLink: String?,
    private val userRepository: UserRepository,
    private val billingRepository: BillingRepository,
    private val notificationRepository: NotificationRepository,
    private val homeFactory: HomeComponent.Factory,
    private val createPostFactory: CreatePostComponent.Factory,
    private val createReplyFactory: CreateReplyComponent.Factory,
    private val postDetailsFactory: PostDetailsComponent.Factory,
    private val mediaViewerFactory: MediaViewerComponent.Factory,
    private val hashtagsFactory: HashtagsComponent.Factory,
    private val storeFactory: StoreComponent.Factory,
    private val aiFactory: AiStudioComponent.Factory,
    private val profileFactory: ProfileComponent.Factory,
    private val repostFactory: CreateRepostComponent.Factory,
    private val exploreFactory: ExploreComponent.Factory,
    private val newsViewerFactory: NewsViewerComponent.Factory,
    private val editProfileFactory: EditProfileComponent.Factory,
    private val addPetFactory: AddPetComponent.Factory,
    private val editPetFactory: EditPetComponent.Factory,
    private val notificationFactory: NotificationComponent.Factory
) : MainFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainFlowComponent.Config>()
    private val dialogNavigation = SlotNavigation<MainFlowComponent.DialogConfig>()

    private val scope = componentScope()

    override val userState: Value<UserState> = userRepository.user
    override val walletState: Value<WalletResponseDto> = billingRepository.wallet

    private val _isDrawerOpen = MutableValue(false)
    override val isDrawerOpen: Value<Boolean> = _isDrawerOpen

    private val _unreadNotificationsCount = MutableValue(0L)
    override val unreadNotificationsCount: Value<Long> = _unreadNotificationsCount

    init {
        scope.launch {
            billingRepository.getWallet()
            if (userRepository.user.value.profile == null) {
                userRepository.getMyProfile()
            }
            notificationRepository.syncToken()
        }
        scope.launch {
            notificationRepository.getUnreadCount()
                .onSuccess { count ->
                    _unreadNotificationsCount.value = count
                }
                .onFailure { error ->
                    Logger.e(error) {"Error getting unread notifications: ${error.message}"}
                }
        }
    }

    override fun setDrawerOpen(isOpen: Boolean) {
        _isDrawerOpen.value = isOpen
    }

    override fun onNotificationsClick() {
        _unreadNotificationsCount.value = 0
        navigation.push(MainFlowComponent.Config.Notifications)
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
                },
                onNavigateToEditProfile = {
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.EditProfile)
                },
                onNavigateToAddPet = {
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.AddPet)
                },
                onNavigateToEditPet = { petId, petData ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.EditPet(petId, petData))
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
                },
                onNavigateToEditProfile = {
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.EditProfile)
                },
                onNavigateToAddPet = {
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.AddPet)
                },
                onNavigateToEditPet = { petId, petData ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.EditPet(petId, petData))
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

        Explore -> MainFlowComponent.Child.Explore(
            exploreFactory(
                componentContext = context,
                onNavigateToPost = { post ->
                    navigation.push(PostDetails(postId = post.id))
                },
                onNavigateToProfile = { userHandle ->
                    navigation.bringToFront(UserProfile(userHandle))
                },
                onNavigateToMediaViewer = { mediaList, index ->
                    navigation.push(MediaViewer(mediaList, index))
                },
                onNavigateToHashtag = { tag ->
                    navigation.push(MainFlowComponent.Config.HashtagsFeed(tag))
                },
                onNavigateToNews = { url -> navigation.push(MainFlowComponent.Config.NewsViewer(url))},
                onNavigateToRepost = { post ->
                    dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
                }
            )
        )

        is MainFlowComponent.Config.NewsViewer -> MainFlowComponent.Child.NewsViewer(
            newsViewerFactory(
                componentContext = context,
                url = config.url,
                onBack = { navigation.pop() }
            )
        )

        MainFlowComponent.Config.Notifications -> MainFlowComponent.Child.Notifications(
            notificationFactory(
                componentContext = context,
                onBack = { navigation.pop() },
                onNavigateToDeepLink = { link ->
                    val config = link.toMainFlowConfig()
                    if (config != null) {
                        navigation.push(config)
                    }
                }
            )
        )

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

        MainFlowComponent.DialogConfig.EditProfile -> MainFlowComponent.DialogChild.EditProfile(
            editProfileFactory(
                componentContext = context,
                initialProfile = userState.value,
                onBack = { dialogNavigation.dismiss() },
                onProfileUpdated = {
                    scope.launch {
                        userRepository.getMyProfile()
                        dialogNavigation.dismiss()
                    }
                }
            )
        )

        MainFlowComponent.DialogConfig.AddPet -> MainFlowComponent.DialogChild.AddPet(
            addPetFactory(
                componentContext = context,
                onBack = { dialogNavigation.dismiss() },
                onPetAdded = {
                    scope.launch {
                        userRepository.getMyProfile()
                        dialogNavigation.dismiss()
                    }
                }
            )
        )

        is MainFlowComponent.DialogConfig.EditPet -> MainFlowComponent.DialogChild.EditPet(
            editPetFactory(
                componentContext = context,
                petId = config.petId,
                initialPetData = config.petData,
                onBack = { dialogNavigation.dismiss() },
                onPetUpdated = {
                    scope.launch {
                        userRepository.getMyProfile()
                        dialogNavigation.dismiss()
                    }
                }
            )
        )
    }

    override fun onDeepLink(link: String) {
        val newConfig = link.toMainFlowConfig() ?: return

        val activeConfig = stack.value.active.configuration

        if (activeConfig == newConfig) {
            return
        }

        navigation.push(newConfig)
    }

    private fun getInitialStack(link: String?): List<MainFlowComponent.Config> {
        val baseStack = listOf(Home)

        val deepLinkConfig = link?.toMainFlowConfig()

        return if (deepLinkConfig != null) {
            baseStack + deepLinkConfig
        } else {
            baseStack
        }
    }

    override fun onTabSelected(tab: MainFlowComponent.Tab) {
        navigation.bringToFront(tab.toConfig())
    }

    override fun onPostClick() = navigation.push(CreatePost())
}