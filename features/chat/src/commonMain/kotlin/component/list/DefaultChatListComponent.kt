package component.list

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import data.ChatDto
import domain.ChatRepository
import domain.UserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.componentScope
import org.example.whiskr.dto.ProfileResponse

@OptIn(FlowPreview::class)
@Inject
class DefaultChatListComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToChat: (Long) -> Unit,
    private val chatRepository: ChatRepository,
    userRepository: UserRepository
) : ChatListComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(ChatListComponent.Model())
    override val model: Value<ChatListComponent.Model> = _model

    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = { page -> chatRepository.getMyChats(page) }
    )

    private val searchQueryFlow = MutableStateFlow("")

    init {
        val user = userRepository.user.value
        _model.update { it.copy(currentUserId = user.profile?.id ?: -1) }

        pagingDelegate.state.subscribe { pagingState ->
            _model.update { current ->
                current.copy(
                    chats = pagingState.items,
                    isRefreshing = pagingState.isRefreshing || pagingState.isLoading,
                    isLoadingMore = pagingState.isLoadingMore
                )
            }
        }

        pagingDelegate.firstLoad()

        scope.launch {
            searchQueryFlow
                .debounce(200)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        search(query)
                    } else {
                        _model.update {
                            it.copy(searchResults = emptyList(), isSearching = false)
                        }
                    }
                }
        }
    }

    override fun onSearchQueryChanged(query: String) {
        _model.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    private fun search(query: String) {
        scope.launch {
            _model.update { it.copy(isSearching = true) }

            chatRepository.searchUsers(query)
                .onSuccess { profiles ->
                    _model.update {
                        it.copy(searchResults = profiles, isSearching = false)
                    }
                }
                .onFailure { error ->
                    Logger.e(error) { "Search failed" }
                    _model.update {
                        it.copy(searchResults = emptyList(), isSearching = false)
                    }
                }
        }
    }

    override fun onRefresh() {
        pagingDelegate.refresh()
    }

    override fun onLoadMore() {
        if (_model.value.searchQuery.isBlank()) {
            pagingDelegate.loadMore()
        }
    }

    override fun onChatClicked(chat: ChatDto) {
        onNavigateToChat(chat.id)
    }

    override fun onUserClicked(profile: ProfileResponse) {
        scope.launch {
            _model.update { it.copy(isSearching = true) }

            chatRepository.getOrCreatePrivateChat(profile.userId)
                .onSuccess { chat ->
                    _model.update { it.copy(isSearching = false) }
                    pagingDelegate.refresh()
                    onNavigateToChat(chat.id)
                }
                .onFailure { error ->
                    _model.update { it.copy(isSearching = false) }
                    Logger.e(error) { "Failed to create chat" }
                }
        }
    }
}