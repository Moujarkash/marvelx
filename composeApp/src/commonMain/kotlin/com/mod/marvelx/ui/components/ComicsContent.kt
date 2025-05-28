package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.Comic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicsContent(
    comics: List<Comic>,
    isLoadingMore: Boolean,
    loadMoreError: String?,
    onComicClick: (String, String) -> Unit,
    onRetryLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    listState: LazyListState
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(comics) { comic ->
                ComicItem(
                    comic = comic,
                    onClick = { onComicClick(comic.id.toString(), comic.title) }
                )
            }

            // Loading more indicator
            if (isLoadingMore) {
                item {
                    LoadingMoreItem()
                }
            }

            // Load more error
            if (loadMoreError != null) {
                item {
                    LoadMoreErrorItem(
                        error = loadMoreError,
                        onRetry = onRetryLoadMore
                    )
                }
            }
        }
    }
}