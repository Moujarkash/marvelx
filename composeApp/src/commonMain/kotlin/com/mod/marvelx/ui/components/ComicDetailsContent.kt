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
import com.mod.marvelx.models.ImageVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailsContent(
    comic: Comic,
    characters: List<Character>,
    isRefreshing: Boolean,
    isCharactersLoading: Boolean,
    isLoadingMoreCharacters: Boolean,
    charactersError: String?,
    loadMoreCharactersError: String?,
    onCharacterClick: (String, String) -> Unit,
    onRefresh: () -> Unit,
    onRefreshCharacters: () -> Unit,
    onRetryLoadMoreCharacters: () -> Unit,
    charactersListState: LazyListState
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            state = charactersListState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Comic Details Section
            item {
                ComicInfoSection(comic = comic)
            }

            // Characters Section Header
            item {
                CharactersSectionHeader(
                    charactersCount = comic.characters.available,
                    isLoading = isCharactersLoading,
                    onRefresh = onRefreshCharacters
                )
            }

            // Characters Error State
            if (charactersError != null && characters.isEmpty()) {
                item {
                    CharactersErrorContent(
                        error = charactersError,
                        onRetry = onRefreshCharacters
                    )
                }
            }
            // Characters Loading State
            else if (isCharactersLoading && characters.isEmpty()) {
                item {
                    CharactersLoadingContent()
                }
            }
            // Characters Empty State
            else if (characters.isEmpty() && !isCharactersLoading) {
                item {
                    CharactersEmptyContent()
                }
            }
            // Characters Content
            else {
                items(characters) { character ->
                    CharacterItem(
                        character = character,
                        imageVariant = ImageVariant.LANDSCAPE_INCREDIBLE,
                        onClick = { onCharacterClick(character.id.toString(), character.name) }
                    )
                }

                // Loading more indicator
                if (isLoadingMoreCharacters) {
                    item {
                        LoadingMoreItem()
                    }
                }

                // Load more error
                if (loadMoreCharactersError != null) {
                    item {
                        LoadMoreErrorItem(
                            error = loadMoreCharactersError,
                            onRetry = onRetryLoadMoreCharacters
                        )
                    }
                }
            }
        }
    }
}