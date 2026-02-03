package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.data.Notification
import org.example.whiskr.domain.NotificationRepository

@Inject
class DefaultNotificationComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToDeepLink: (String) -> Unit,
    private val notificationRepository: NotificationRepository
) : NotificationComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = { page ->
            notificationRepository.getNotifications(page)
        }
    )

    private val _model = MutableValue(
        NotificationComponent.Model(
            items = emptyList(),
            isRefreshing = false,
            isLoadingMore = false,
            isError = false
        )
    )
    override val model: Value<NotificationComponent.Model> = _model

    init {
        val observer = { pagingState: PagingDelegate.State<Notification> ->
            _model.update { current ->
                current.copy(
                    items = pagingState.items,
                    isRefreshing = pagingState.isRefreshing || pagingState.isLoading,
                    isLoadingMore = pagingState.isLoadingMore,
                    isError = pagingState.isError
                )
            }
        }
        pagingDelegate.state.subscribe(observer)

        pagingDelegate.firstLoad()

        markAllAsRead()
    }

    override fun onRefresh() {
        pagingDelegate.refresh()
        markAllAsRead()
    }

    override fun onLoadMore() {
        pagingDelegate.loadMore()
    }

    override fun onNotificationClicked(item: Notification) {
        if (!item.isRead) {
            pagingDelegate.updateItems { currentList ->
                currentList.map {
                    if (it.id == item.id) it.copy(isRead = true) else it
                }
            }
        }

        item.deepLink?.let { link ->
            onNavigateToDeepLink(link)
        }
    }

    override fun onMarkAllReadClicked() {
        markAllAsRead()
        pagingDelegate.updateItems { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    override fun onBackClicked() {
        onBack()
    }

    private fun markAllAsRead() {
        scope.launch {
            notificationRepository.markAllAsRead()
        }
    }
}