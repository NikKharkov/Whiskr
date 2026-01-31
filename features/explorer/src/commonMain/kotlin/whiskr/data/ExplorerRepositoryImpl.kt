package org.example.whiskr.data

import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.ExplorerRepository
import org.example.whiskr.dto.PagedResponse

@Inject
class ExplorerRepositoryImpl(
    private val explorerApiService: ExplorerApiService
) : ExplorerRepository {

    override suspend fun getTrending(page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            explorerApiService.getTrending(page)
        }
    }

    override suspend fun searchPosts(query: String, page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            if (query.isBlank()) {
                PagedResponse(
                    content = emptyList(),
                    last = true,
                    totalElements = 0,
                    totalPages = 0,
                    number = page,
                    size = 20
                )
            } else {
                explorerApiService.searchPosts(query, page)
            }
        }
    }
}