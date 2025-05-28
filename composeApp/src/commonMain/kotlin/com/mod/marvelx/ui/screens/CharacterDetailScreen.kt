package com.mod.marvelx.ui.screens

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.mod.marvelx.ui.characters.CharacterDetailsIntent
import com.mod.marvelx.ui.components.CharacterDetailsContent
import com.mod.marvelx.ui.components.EmptyContent
import com.mod.marvelx.ui.components.ErrorContent
import com.mod.marvelx.ui.components.LoadingContent
import com.mod.marvelx.viewModels.CharacterDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CharacterDetailsScreen(
    characterId: String,
    onComicClick: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailsViewModel = koinViewModel<CharacterDetailsViewModel> { parametersOf(characterId.toInt()) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val comicsListState = rememberLazyListState()

    // Handle comics pagination
    LaunchedEffect(comicsListState) {
        snapshotFlow { comicsListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.comics.size - 3 &&
                    uiState.hasMoreComics &&
                    !uiState.isLoadingMoreComics &&
                    !uiState.isLoading &&
                    !uiState.isComicsLoading) {
                    viewModel.handleIntent(CharacterDetailsIntent.LoadMoreComics)
                }
            }
    }

    when {
        uiState.isLoading -> {
            LoadingContent()
        }
        uiState.error != null && uiState.character == null -> {
            ErrorContent(
                error = uiState.error!!,
                onRetry = { viewModel.handleIntent(CharacterDetailsIntent.LoadCharacterDetails) }
            )
        }
        uiState.isEmpty -> {
            EmptyContent(searchQuery = "")
        }
        else -> {
            CharacterDetailsContent(
                character = uiState.character!!,
                comics = uiState.comics,
                isRefreshing = uiState.isRefreshing,
                isComicsLoading = uiState.isComicsLoading,
                isLoadingMoreComics = uiState.isLoadingMoreComics,
                comicsError = uiState.comicsError,
                loadMoreComicsError = uiState.loadMoreComicsError,
                onComicClick = onComicClick,
                onRefresh = { viewModel.handleIntent(CharacterDetailsIntent.RefreshCharacterDetails) },
                onRefreshComics = { viewModel.handleIntent(CharacterDetailsIntent.RefreshComics) },
                onRetryLoadMoreComics = { viewModel.handleIntent(CharacterDetailsIntent.RetryLoadMoreComics) },
                comicsListState = comicsListState
            )
        }
    }
}