package org.example.whiskr

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.whiskr.dto.PagedResponse

class PagingDelegate<T>(
    private val scope: CoroutineScope,
    initialItems: List<T> = emptyList(),
    private val loader: suspend (page: Int) -> Result<PagedResponse<T>>
) {
    data class State<T>(
        val items: List<T> = emptyList(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val isError: Boolean = false,
        val isEndOfList: Boolean = false,
        val currentPage: Int = 0
    )

    private val _state = MutableValue(State(items = initialItems))
    val state: Value<State<T>> = _state

    fun firstLoad() {
        if (_state.value.items.isNotEmpty()) return
        load(isRefresh = true, isFirstInit = true)
    }

    fun refresh() {
        load(isRefresh = true)
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoadingMore || s.isEndOfList || s.isLoading || s.isRefreshing) return
        load(isRefresh = false)
    }

    private fun load(isRefresh: Boolean, isFirstInit: Boolean = false) {
        scope.launch {
            _state.update { s ->
                if (isRefresh) s.copy(isRefreshing = !isFirstInit, isLoading = isFirstInit, isError = false)
                else s.copy(isLoadingMore = true, isError = false)
            }

            val targetPage = if (isRefresh) 0 else _state.value.currentPage + 1

            loader(targetPage)
                .onSuccess { response ->
                    _state.update { s ->
                        val newItems = if (isRefresh) response.content else s.items + response.content

                        s.copy(
                            items = newItems,
                            currentPage = response.number,
                            isRefreshing = false,
                            isLoadingMore = false,
                            isLoading = false,
                            isEndOfList = response.last
                        )
                    }
                }
                .onFailure {
                    _state.update { s ->
                        s.copy(
                            isRefreshing = false,
                            isLoadingMore = false,
                            isLoading = false,
                            isError = true
                        )
                    }
                }
        }
    }

    fun updateItems(transform: (List<T>) -> List<T>) {
        _state.update { it.copy(items = transform(it.items)) }
    }

    fun prependItem(item: T) {
        updateItems { listOf(item) + it }
    }

    fun tryReplace(predicate: (T) -> Boolean, newItem: T): Boolean {
        val currentList = _state.value.items.toMutableList()
        val index = currentList.indexOfFirst(predicate)

        if (index != -1) {
            currentList[index] = newItem
            _state.update { it.copy(items = currentList) }
            return true
        }
        return false
    }
}