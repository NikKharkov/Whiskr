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
import component.detail.ChatDetailComponent
import component.ProfileComponent
import component.add_pet.AddPetComponent
import component.edit_pet.EditPetComponent
import component.edit_profile.EditProfileComponent
import component.list.ChatListComponent
import domain.UserRepository
import domain.UserState
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.component.viewer.NewsViewerComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.WalletResponseDto
import org.example.whiskr.domain.BillingRepository
import org.example.whiskr.domain.NotificationRepository
import org.example.whiskr.dto.Media
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
    private val notificationFactory: NotificationComponent.Factory,
    private val chatDetailFactory: ChatDetailComponent.Factory,
    private val chatListFactory: ChatListComponent.Factory
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
                .onSuccess { _unreadNotificationsCount.value = it }
                .onFailure { Logger.e(it) { "Error getting unread count" } }
        }
    }

    private fun navigateToPost(post: Post) {
        navigation.push(MainFlowComponent.Config.PostDetails(post.id))
    }

    private fun navigateToProfile(handle: String) {
        navigation.bringToFront(MainFlowComponent.Config.UserProfile(handle))
    }

    private fun navigateToMedia(media: List<Media>, index: Int) {
        navigation.push(MainFlowComponent.Config.MediaViewer(media, index))
    }

    private fun navigateToHashtag(tag: String) {
        navigation.push(MainFlowComponent.Config.HashtagsFeed(tag))
    }

    private fun navigateToChat(chatId: Long = -1L, userId: Long = -1L) {
        navigation.push(MainFlowComponent.Config.ChatDetail(chatId, userId))
    }

    private fun openRepostDialog(post: Post) {
        dialogNavigation.activate(MainFlowComponent.DialogConfig.CreateRepost(post))
    }

    override fun setDrawerOpen(isOpen: Boolean) {
        _isDrawerOpen.value = isOpen
    }

    override fun onNotificationsClick() {
        _unreadNotificationsCount.value = 0
        navigation.push(MainFlowComponent.Config.Notifications)
    }

    override fun onPostClick() = navigation.push(MainFlowComponent.Config.CreatePost())

    override fun onTabSelected(tab: MainFlowComponent.Tab) {
        navigation.bringToFront(tab.toConfig())
    }

    override fun onDeepLink(link: String) {
        val newConfig = link.toMainFlowConfig() ?: return
        if (stack.value.active.configuration != newConfig) {
            navigation.push(newConfig)
        }
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

        MainFlowComponent.Config.Home -> MainFlowComponent.Child.Home(
            homeFactory(
                componentContext = context,
                onNavigateToCreatePost = { navigation.push(MainFlowComponent.Config.CreatePost()) },
                onNavigateToProfile = ::navigateToProfile,
                onNavigateToComments = ::navigateToPost,
                onNavigateToMediaViewer = ::navigateToMedia,
                onNavigateToHashtag = ::navigateToHashtag,
                onNavigateToRepost = ::openRepostDialog
            )
        )

        MainFlowComponent.Config.Explore -> MainFlowComponent.Child.Explore(
            exploreFactory(
                componentContext = context,
                onNavigateToPost = ::navigateToPost,
                onNavigateToProfile = ::navigateToProfile,
                onNavigateToMediaViewer = ::navigateToMedia,
                onNavigateToHashtag = ::navigateToHashtag,
                onNavigateToNews = { url -> navigation.push(MainFlowComponent.Config.NewsViewer(url)) },
                onNavigateToRepost = ::openRepostDialog
            )
        )

        is MainFlowComponent.Config.PostDetails -> MainFlowComponent.Child.PostDetails(
            postDetailsFactory(
                componentContext = context,
                postId = config.postId,
                onBack = navigation::pop,
                onNavigateToReply = { post ->
                    navigation.push(
                        MainFlowComponent.Config.CreateReply(
                            post
                        )
                    )
                },
                onNavigateToPostDetails = ::navigateToPost,
                onNavigateToMediaViewer = ::navigateToMedia,
                onNavigateToHashtag = ::navigateToHashtag,
                onNavigateToProfile = ::navigateToProfile,
                onNavigateToRepost = ::openRepostDialog
            )
        )

        is MainFlowComponent.Config.UserProfile -> createProfileChild(
            context,
            config.handle,
            isMe = false
        )

        MainFlowComponent.Config.Profile -> createProfileChild(
            context,
            handle = userState.value.profile?.handle.orEmpty(),
            isMe = true
        )

        MainFlowComponent.Config.Messages -> MainFlowComponent.Child.Messages(
            chatListFactory(
                   componentContext = context,
                   onNavigateToChat = { chatId -> navigateToChat(chatId = chatId) }
               )
        )

        is MainFlowComponent.Config.ChatDetail -> MainFlowComponent.Child.ChatDetail(
            chatDetailFactory(
                componentContext = context,
                chatId = config.chatId,
                userId = config.userId,
                onBack = navigation::pop,
                onNavigateToProfile = ::navigateToProfile,
                onNavigateToMediaViewer = ::navigateToMedia
            )
        )

        MainFlowComponent.Config.Notifications -> MainFlowComponent.Child.Notifications(
            notificationFactory(
                componentContext = context,
                onBack = navigation::pop,
                onNavigateToDeepLink = ::onDeepLink
            )
        )

        MainFlowComponent.Config.AiStudio -> MainFlowComponent.Child.AiStudio(
            aiFactory(
                componentContext = context,
                initialBalance = walletState.value.balance,
                onNavigateToMediaViewer = { url ->
                    navigateToMedia(
                        listOf(url.toAiPostMedia()),
                        0
                    )
                },
                onNavigateToCreatePost = { url ->
                    navigation.push(
                        MainFlowComponent.Config.CreatePost(
                            imageUrl = url
                        )
                    )
                }
            )
        )

        is MainFlowComponent.Config.CreatePost -> MainFlowComponent.Child.CreatePost(
            createPostFactory(context, config.imageUrl, { navigation.pop() }, navigation::pop)
        )

        is MainFlowComponent.Config.CreateReply -> MainFlowComponent.Child.CreateReply(
            createReplyFactory(context, config.targetPost, { navigation.pop() }, navigation::pop)
        )

        is MainFlowComponent.Config.MediaViewer -> MainFlowComponent.Child.MediaViewer(
            mediaViewerFactory(context, config.media, config.index, navigation::pop)
        )

        is MainFlowComponent.Config.NewsViewer -> MainFlowComponent.Child.NewsViewer(
            newsViewerFactory(context, config.url, navigation::pop)
        )

        MainFlowComponent.Config.Store -> MainFlowComponent.Child.Store(
            storeFactory(
                context,
                navigation::pop
            )
        )

        is MainFlowComponent.Config.HashtagsFeed -> MainFlowComponent.Child.HashtagsFeed(
            hashtagsFactory(
                componentContext = context,
                hashtag = config.hashtag,
                onBack = navigation::pop,
                onNavigateToComments = ::navigateToPost,
                onNavigateToMediaViewer = ::navigateToMedia,
                onNavigateToHashtag = ::navigateToHashtag,
                onNavigateToProfile = ::navigateToProfile,
                onNavigateToRepost = ::openRepostDialog
            )
        )

        MainFlowComponent.Config.Games -> MainFlowComponent.Child.Games(Unit) // TODO("Games")
    }

    private fun createProfileChild(
        context: ComponentContext,
        handle: String,
        isMe: Boolean
    ): MainFlowComponent.Child.Profile {
        return MainFlowComponent.Child.Profile(
            profileFactory(
                componentContext = context,
                handle = handle,
                onBack = navigation::pop,
                onNavigateToPost = ::navigateToPost,
                onNavigateToUserProfile = ::navigateToProfile,
                onNavigateToMediaViewer = ::navigateToMedia,
                onNavigateToHashtag = ::navigateToHashtag,
                onNavigateToRepost = ::openRepostDialog,
                onNavigateToEditProfile = { dialogNavigation.activate(MainFlowComponent.DialogConfig.EditProfile) },
                onNavigateToAddPet = { dialogNavigation.activate(MainFlowComponent.DialogConfig.AddPet) },
                onNavigateToEditPet = { id, data ->
                    dialogNavigation.activate(
                        MainFlowComponent.DialogConfig.EditPet(
                            id,
                            data
                        )
                    )
                },
                onSendMessageClick = { userId -> navigateToChat(userId = userId) }
            ),
            isMe = isMe
        )
    }

    private fun createDialogChild(
        config: MainFlowComponent.DialogConfig,
        context: ComponentContext
    ): MainFlowComponent.DialogChild = when (config) {
        is MainFlowComponent.DialogConfig.CreateRepost -> MainFlowComponent.DialogChild.CreateRepost(
            repostFactory(
                context,
                config.targetPost,
                dialogNavigation::dismiss,
                dialogNavigation::dismiss
            )
        )

        MainFlowComponent.DialogConfig.EditProfile -> MainFlowComponent.DialogChild.EditProfile(
            editProfileFactory(
                componentContext = context,
                initialProfile = userState.value,
                onBack = dialogNavigation::dismiss,
                onProfileUpdated = { scope.launch { userRepository.getMyProfile(); dialogNavigation.dismiss() } }
            )
        )

        MainFlowComponent.DialogConfig.AddPet -> MainFlowComponent.DialogChild.AddPet(
            addPetFactory(
                componentContext = context,
                onBack = dialogNavigation::dismiss,
                onPetAdded = { scope.launch { userRepository.getMyProfile(); dialogNavigation.dismiss() } }
            )
        )

        is MainFlowComponent.DialogConfig.EditPet -> MainFlowComponent.DialogChild.EditPet(
            editPetFactory(
                componentContext = context,
                petId = config.petId,
                initialPetData = config.petData,
                onBack = dialogNavigation::dismiss,
                onPetUpdated = { scope.launch { userRepository.getMyProfile(); dialogNavigation.dismiss() } }
            )
        )
    }

    private fun getInitialStack(link: String?): List<MainFlowComponent.Config> {
        val base = listOf(MainFlowComponent.Config.Home)
        val deep = link?.toMainFlowConfig()
        return if (deep != null) base + deep else base
    }
}