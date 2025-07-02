package com.example.mam.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow

class GenericPagingSource<T : Any>(
    private val sort: List<String> = emptyList(),
    private val filter: String = "",
    private val fetchData: suspend (page: Int, size: Int, sort: List<String>, filter: String) -> List<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val data = fetchData(
                page,
                pageSize,
                sort,
                filter
            )

            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}

fun <T : Any> createPagingFlow(
    filter: String = "",
    sort: List<String> = emptyList(),
    fetch: suspend (page: Int, size: Int, sort: List<String>, filter: String) -> List<T>
): Flow<PagingData<T>> {
    return Pager(PagingConfig(pageSize = 20)) {
        GenericPagingSource(
            sort = sort,
            filter = filter,
            fetchData = { page, size, sort, filter ->
                fetch(page, size, sort, filter)
            }
        )
    }.flow
}
