package component.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import data.ChatDto
import org.example.whiskr.dto.ProfileResponse

interface ChatListComponent {
    val model: Value<Model>

    fun onSearchQueryChanged(query: String)
    fun onChatClicked(chat: ChatDto)
    fun onUserClicked(profile: ProfileResponse)
    fun onRefresh()
    fun onLoadMore()

    data class Model(
        val chats: List<ChatDto> = emptyList(),
        val searchResults: List<ProfileResponse> = emptyList(),
        val searchQuery: String = "",
        val isSearching: Boolean = false,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val currentUserId: Long = -1L
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToChat: (Long) -> Unit
        ): ChatListComponent
    }
}