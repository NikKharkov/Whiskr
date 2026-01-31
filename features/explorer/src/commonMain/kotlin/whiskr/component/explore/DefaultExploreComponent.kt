package org.example.whiskr.component.explore

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.prof18.rssparser.RssParser
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.AnimalNews
import org.example.whiskr.domain.ExplorerRepository

@Inject
class DefaultExploreComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToPost: (Post) -> Unit,
    @Assisted private val onNavigateToProfile: (String) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToUrl: (String) -> Unit,
    @Assisted private val onNavigateToRepost: (Post) -> Unit,
    private val explorerRepository: ExplorerRepository,
    postListFactory: PostListComponent.Factory
) : ExploreComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    val rssParser: RssParser = RssParser()

    private val _model = MutableValue(ExploreComponent.Model())
    override val model: Value<ExploreComponent.Model> = _model

    private var activeQuery: String = ""
    private var searchJob: Job? = null

    override val postsComponent: PostListComponent = postListFactory(
        componentContext = childContext("ExploreFeed"),
        loader = { page ->
            if (activeQuery.isBlank()) {
                explorerRepository.getTrending(page)
            } else {
                explorerRepository.searchPosts(activeQuery, page)
            }
        },
        onNavigateToComments = onNavigateToPost,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = onNavigateToHashtag,
        onNavigateToRepost = onNavigateToRepost
    )

    init {
        fetchAnimalNews()
    }

    override fun fetchAnimalNews() {
        scope.launch {
            val channel = rssParser.getRssChannel("https://www.lovemeow.com/feeds/feed.rss")
            val news = channel.items.map { item ->
                AnimalNews(
                    title = item.title ?: "",
                    link = item.link ?: "",
                    imageUrl = item.image
                )
            }
            _model.update { it.copy(news = news) }
        }
    }

    override fun onQueryChanged(query: String) {
        _model.value = _model.value.copy(query = query)
        searchJob?.cancel()

        if (query.isBlank()) {
            onBackClick()
            return
        }

        searchJob = scope.launch {
            delay(300L)
            onSearch(query)
        }
    }

    override fun onSearch(query: String) {
        if (query.isBlank()) return
        if (activeQuery == query) return
        activeQuery = query
        _model.value = _model.value.copy(isSearching = true)
        postsComponent.onRefresh()
    }

    override fun onBackClick() {
        if (_model.value.isSearching) {
            activeQuery = ""
            _model.value = _model.value.copy(query = "", isSearching = false)
            postsComponent.onRefresh()
        }
    }

    override fun onNewsClick(url: String) = onNavigateToUrl(url)
}