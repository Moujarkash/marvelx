package com.mod.marvelx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mod.marvelx.repositories.MarvelRepository
import com.mod.marvelx.ui.comics.ComicsIntent
import com.mod.marvelx.ui.comics.ComicsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComicsViewModel(
    private val marvelRepository: MarvelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComicsUiState())
    val uiState: StateFlow<ComicsUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20

    init {
        handleIntent(ComicsIntent.LoadComics)
    }

    fun handleIntent(intent: ComicsIntent) {
        when (intent) {
            is ComicsIntent.LoadComics -> loadComics()
            is ComicsIntent.RefreshComics -> refreshComics()
            is ComicsIntent.LoadMoreComics -> loadMoreComics()
            is ComicsIntent.SearchComics -> searchComics(intent.query)
            is ComicsIntent.ClearError -> clearError()
            is ComicsIntent.ClearLoadMoreError -> clearLoadMoreError()
            is ComicsIntent.RetryLoadMore -> retryLoadMore()
        }
    }

    private fun loadComics(forceRefresh: Boolean = false) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }

                val result = marvelRepository.getComics(
                    offset = if (forceRefresh) 0 else currentOffset,
                    limit = pageSize,
                    titleStartsWith = _uiState.value.searchQuery.takeIf { it.isNotBlank() },
                    forceRefresh = forceRefresh
                )

                val newComics = if (forceRefresh) {
                    result.items
                } else {
                    _uiState.value.comics + result.items
                }

                currentOffset = if (forceRefresh) result.limit else currentOffset + result.items.size

                updateState {
                    copy(
                        comics = newComics,
                        isLoading = false,
                        hasMore = result.hasMore,
                        isEmpty = newComics.isEmpty(),
                        error = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred",
                        isEmpty = comics.isEmpty()
                    )
                }
            }
        }
    }

    private fun loadMoreComics() {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || !currentState.hasMore || currentState.isLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoadingMore = true,
                        loadMoreError = null
                    )
                }

                val result = marvelRepository.getComics(
                    offset = currentOffset,
                    limit = pageSize,
                    titleStartsWith = _uiState.value.searchQuery.takeIf { it.isNotBlank() },
                    forceRefresh = false
                )

                currentOffset += result.items.size

                updateState {
                    copy(
                        comics = comics + result.items,
                        isLoadingMore = false,
                        hasMore = result.hasMore,
                        loadMoreError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoadingMore = false,
                        loadMoreError = e.message ?: "Failed to load more comics"
                    )
                }
            }
        }
    }

    private fun searchComics(query: String) {
        val currentState = _uiState.value
        if (currentState.searchQuery == query) return

        updateState { copy(searchQuery = query) }
        currentOffset = 0
        loadComics(forceRefresh = true)
    }

    private fun refreshComics() {
        currentOffset = 0
        loadComics(forceRefresh = true)
    }

    private fun clearError() {
        updateState { copy(error = null) }
    }

    private fun clearLoadMoreError() {
        updateState { copy(loadMoreError = null) }
    }

    private fun retryLoadMore() {
        clearLoadMoreError()
        loadMoreComics()
    }

    private fun updateState(update: ComicsUiState.() -> ComicsUiState) {
        _uiState.value = _uiState.value.update()
    }
}