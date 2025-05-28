package com.mod.marvelx.ui.screens

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.mod.marvelx.ui.comics.ComicDetailsIntent
import com.mod.marvelx.ui.components.ComicDetailsContent
import com.mod.marvelx.ui.components.EmptyContent
import com.mod.marvelx.ui.components.ErrorContent
import com.mod.marvelx.ui.components.LoadingContent
import com.mod.marvelx.viewModels.ComicDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ComicDetailsScreen(
    comicId: String,
    onCharacterClick: (String, String) -> Unit,
    viewModel: ComicDetailsViewModel = koinViewModel<ComicDetailsViewModel> { parametersOf(comicId.toInt()) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val charactersListState = rememberLazyListState()

    // Handle characters pagination
    LaunchedEffect(charactersListState) {
        snapshotFlow { charactersListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.characters.size - 3 &&
                    uiState.hasMoreCharacters &&
                    !uiState.isLoadingMoreCharacters &&
                    !uiState.isLoading &&
                    !uiState.isCharactersLoading) {
                    viewModel.handleIntent(ComicDetailsIntent.LoadMoreCharacters)
                }
            }
    }

    when {
        uiState.isLoading -> {
            LoadingContent()
        }
        uiState.error != null && uiState.comic == null -> {
            ErrorContent(
                error = uiState.error!!,
                onRetry = { viewModel.handleIntent(ComicDetailsIntent.LoadComicDetails) }
            )
        }
        uiState.isEmpty -> {
            EmptyContent(searchQuery = "")
        }
        else -> {
            ComicDetailsContent(
                comic = uiState.comic!!,
                characters = uiState.characters,
                isRefreshing = uiState.isRefreshing,
                isCharactersLoading = uiState.isCharactersLoading,
                isLoadingMoreCharacters = uiState.isLoadingMoreCharacters,
                charactersError = uiState.charactersError,
                loadMoreCharactersError = uiState.loadMoreCharactersError,
                onCharacterClick = onCharacterClick,
                onRefresh = { viewModel.handleIntent(ComicDetailsIntent.RefreshComicDetails) },
                onRefreshCharacters = { viewModel.handleIntent(ComicDetailsIntent.RefreshCharacters) },
                onRetryLoadMoreCharacters = { viewModel.handleIntent(ComicDetailsIntent.RetryLoadMoreCharacters) },
                charactersListState = charactersListState
            )
        }
    }
}