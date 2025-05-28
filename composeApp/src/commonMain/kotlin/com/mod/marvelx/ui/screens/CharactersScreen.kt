package com.mod.marvelx.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.characters.CharactersIntent
import com.mod.marvelx.ui.components.CharactersContent
import com.mod.marvelx.ui.components.EmptyContent
import com.mod.marvelx.ui.components.ErrorContent
import com.mod.marvelx.ui.components.LoadingContent
import com.mod.marvelx.viewModels.CharactersViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (String, String) -> Unit,
    viewModel: CharactersViewModel = koinViewModel<CharactersViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
    }

    // Handle pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.characters.size - 6 && // Load earlier for grid
                    uiState.hasMore &&
                    !uiState.isLoadingMore &&
                    !uiState.isLoading) {
                    viewModel.handleIntent(CharactersIntent.LoadMoreCharacters)
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
            // Search Bar
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = uiState.searchQuery,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        onQueryChange = { query ->
                            viewModel.handleIntent(CharactersIntent.SetSearchQuery(query))
                        },
                        onSearch = { _ ->
                            viewModel.handleIntent(CharactersIntent.SearchCharacters)
                            focusManager.clearFocus(force = true)
                            keyboardController?.hide()
                        },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = {
                            Text("Search characters...")
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
                                        viewModel.handleIntent(CharactersIntent.SetSearchQuery(""))
                                        viewModel.handleIntent(CharactersIntent.SearchCharacters)
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
                uiState.isLoading && uiState.characters.isEmpty() -> {
                    LoadingContent()
                }
                uiState.error != null && uiState.characters.isEmpty() -> {
                    ErrorContent(
                        error = uiState.error!!,
                        onRetry = { viewModel.handleIntent(CharactersIntent.LoadCharacters) }
                    )
                }
                uiState.isEmpty -> {
                    EmptyContent(searchQuery = uiState.searchQuery)
                }
                else -> {
                    CharactersContent(
                        characters = uiState.characters,
                        isLoadingMore = uiState.isLoadingMore,
                        loadMoreError = uiState.loadMoreError,
                        onCharacterClick = onCharacterClick,
                        onRetryLoadMore = { viewModel.handleIntent(CharactersIntent.RetryLoadMore) },
                        onRefresh = { viewModel.handleIntent(CharactersIntent.RefreshCharacters) },
                        isRefreshing = uiState.isLoading,
                        listState = listState
                    )
                }
            }
        }
    }
}