package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.list.ChatListComponent
import org.example.whiskr.layouts.CollapsingTopBarScaffold
import org.example.whiskr.layouts.pagingItems
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.stringResource
import ui.components.list.ChatListItem
import ui.components.list.ChatSearchBar
import ui.components.list.EmptyStateView
import ui.components.list.UserSearchItem
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.no_users_found
import whiskr.features.chat.generated.resources.tab_messages

@Composable
fun MessagesScreen(
    component: ChatListComponent
) {
    val model by component.model.subscribeAsState()
    val isSearchMode = model.searchQuery.isNotEmpty()

    CollapsingTopBarScaffold(
        topBarContentHeight = 120.dp,
        useStatusBarPadding = true,
        topBar = { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(Res.string.tab_messages),
                    style = WhiskrTheme.typography.h1,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                ChatSearchBar(
                    query = model.searchQuery,
                    onQueryChange = component::onSearchQueryChanged
                )
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = if (isSearchMode) 16.dp else 80.dp)
        ) {
            when {
                isSearchMode && model.isSearching -> {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = WhiskrTheme.colors.primary)
                        }
                    }
                }

                isSearchMode && model.searchResults.isEmpty() -> {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_users_found),
                                color = WhiskrTheme.colors.secondary
                            )
                        }
                    }
                }

                isSearchMode -> {
                    items(
                        items = model.searchResults,
                        key = { it.userId }
                    ) { profile ->
                        UserSearchItem(
                            profile = profile,
                            onClick = { component.onUserClicked(profile) }
                        )
                    }
                }

                model.isRefreshing && model.chats.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = WhiskrTheme.colors.primary)
                        }
                    }
                }

                model.chats.isEmpty() -> {
                    item {
                        EmptyStateView()
                    }
                }

                else -> {
                    pagingItems(
                        items = model.chats,
                        isLoadingMore = model.isLoadingMore,
                        isEndOfList = false,
                        onLoadMore = component::onLoadMore,
                        key = { it.id }
                    ) { _, chat ->
                        ChatListItem(
                            chat = chat,
                            currentUserId = model.currentUserId,
                            onClick = { component.onChatClicked(chat) }
                        )

                        HorizontalDivider(
                            color = WhiskrTheme.colors.surface.copy(alpha = 0.05f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 88.dp)
                        )
                    }
                }
            }
        }
    }
}