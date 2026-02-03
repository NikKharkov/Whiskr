package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.Notification

interface NotificationComponent {

    val model: Value<Model>

    fun onBackClicked()
    fun onRefresh()
    fun onLoadMore()
    fun onNotificationClicked(item: Notification)
    fun onMarkAllReadClicked()

    data class Model(
        val items: List<Notification>,
        val isRefreshing: Boolean,
        val isLoadingMore: Boolean,
        val isError: Boolean
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: () -> Unit,
            onNavigateToDeepLink: (String) -> Unit
        ): NotificationComponent
    }
}