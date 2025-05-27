package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersContent(
    characters: List<Character>,
    isLoadingMore: Boolean,
    loadMoreError: String?,
    onCharacterClick: (String) -> Unit,
    onRetryLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    listState: LazyGridState
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(characters) { character ->
                CharacterItem(
                    character = character,
                    onClick = { onCharacterClick(character.id.toString()) }
                )
            }

            // Loading more indicator - spans full width
            if (isLoadingMore) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LoadingMoreItem()
                }
            }

            // Load more error - spans full width
            if (loadMoreError != null) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LoadMoreErrorItem(
                        error = loadMoreError,
                        onRetry = onRetryLoadMore
                    )
                }
            }
        }
    }
}