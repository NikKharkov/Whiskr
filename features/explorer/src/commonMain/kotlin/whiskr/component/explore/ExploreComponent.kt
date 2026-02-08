package org.example.whiskr.component.explore

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.dto.Media
import org.example.whiskr.domain.AnimalNews

interface ExploreComponent {

    val model: Value<Model>
    val postsComponent: PostListComponent

    fun onQueryChanged(query: String)
    fun onSearch(query: String)
    fun onBackClick()
    fun fetchAnimalNews()
    fun onNewsClick(url: String)

    data class Model(
        val query: String = "",
        val isSearching: Boolean = false,
        val news: List<AnimalNews> = emptyList()
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToPost: (Post) -> Unit,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToNews: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit
        ): ExploreComponent
    }
}