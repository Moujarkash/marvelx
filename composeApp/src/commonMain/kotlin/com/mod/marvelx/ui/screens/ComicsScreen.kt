package com.mod.marvelx.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.comics.ComicsIntent
import com.mod.marvelx.ui.components.ComicsContent
import com.mod.marvelx.ui.components.EmptyContent
import com.mod.marvelx.ui.components.ErrorContent
import com.mod.marvelx.ui.components.LoadingContent
import com.mod.marvelx.viewModels.ComicsViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicsScreen(
    onComicClick: (String, String) -> Unit,
    viewModel: ComicsViewModel = koinViewModel<ComicsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
    }

    // Handle pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.comics.size - 3 &&
                    uiState.hasMore &&
                    !uiState.isLoadingMore &&
                    !uiState.isLoading) {
                    viewModel.handleIntent(ComicsIntent.LoadMoreComics)
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val focusRequester = remember { FocusRequester() }
            // Search Bar
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = uiState.searchQuery,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        onQueryChange = { query ->
                            viewModel.handleIntent(ComicsIntent.SetSearchQuery(query))
                        },
                        onSearch = { _ ->
                            viewModel.handleIntent(ComicsIntent.SearchComics)
                            focusManager.clearFocus(force = true)
                            keyboardController?.hide()
                        },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = {
                            Text("Search comics...")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        viewModel.handleIntent(ComicsIntent.SetSearchQuery(""))
                                        viewModel.handleIntent(ComicsIntent.SearchComics)
                                        focusManager.clearFocus(force = true)
                                        keyboardController?.hide()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = { /* Handle expanded state change */ },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {}

            // Content
            when {
                uiState.isLoading && uiState.comics.isEmpty() -> {
                    LoadingContent()
                }
                uiState.error != null && uiState.comics.isEmpty() -> {
                    ErrorContent(
                        error = uiState.error!!,
                        onRetry = { viewModel.handleIntent(ComicsIntent.LoadComics) }
                    )
                }
                uiState.isEmpty -> {
                    EmptyContent(searchQuery = uiState.searchQuery)
                }
                else -> {
                    ComicsContent(
                        comics = uiState.comics,
                        isLoadingMore = uiState.isLoadingMore,
                        loadMoreError = uiState.loadMoreError,
                        onComicClick = onComicClick,
                        onRetryLoadMore = { viewModel.handleIntent(ComicsIntent.RetryLoadMore) },
                        onRefresh = { viewModel.handleIntent(ComicsIntent.RefreshComics) },
                        isRefreshing = uiState.isLoading,
                        listState = listState
                    )
                }
            }
        }
    }
}