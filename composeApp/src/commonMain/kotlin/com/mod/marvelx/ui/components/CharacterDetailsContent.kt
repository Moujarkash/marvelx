package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Comic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsContent(
    character: Character,
    comics: List<Comic>,
    isRefreshing: Boolean,
    isComicsLoading: Boolean,
    isLoadingMoreComics: Boolean,
    comicsError: String?,
    loadMoreComicsError: String?,
    onComicClick: (String, String) -> Unit,
    onRefresh: () -> Unit,
    onRefreshComics: () -> Unit,
    onRetryLoadMoreComics: () -> Unit,
    comicsListState: LazyListState
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = comicsListState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Character Details Section
            item {
                CharacterInfoSection(character = character)
            }

            // Comics Section Header
            item {
                ComicsSectionHeader(
                    comicsCount = character.comics.available,
                    isLoading = isComicsLoading,
                    onRefresh = onRefreshComics
                )
            }

            // Comics Error State
            if (comicsError != null && comics.isEmpty()) {
                item {
                    ComicsErrorContent(
                        error = comicsError,
                        onRetry = onRefreshComics
                    )
                }
            }
            // Comics Loading State
            else if (isComicsLoading && comics.isEmpty()) {
                item {
                    ComicsLoadingContent()
                }
            }
            // Comics Empty State
            else if (comics.isEmpty() && !isComicsLoading) {
                item {
                    ComicsEmptyContent()
                }
            }
            // Comics Content
            else {
                items(comics) { comic ->
                    ComicItem(
                        comic = comic,
                        onClick = { onComicClick(comic.id.toString(), comic.title) }
                    )
                }

                // Loading more indicator
                if (isLoadingMoreComics) {
                    item {
                        LoadingMoreItem()
                    }
                }

                // Load more error
                if (loadMoreComicsError != null) {
                    item {
                        LoadMoreErrorItem(
                            error = loadMoreComicsError,
                            onRetry = onRetryLoadMoreComics
                        )
                    }
                }
            }
        }
    }
}