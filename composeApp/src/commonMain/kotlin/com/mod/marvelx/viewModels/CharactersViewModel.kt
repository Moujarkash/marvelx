package com.mod.marvelx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mod.marvelx.repositories.MarvelRepository
import com.mod.marvelx.ui.characters.CharactersIntent
import com.mod.marvelx.ui.characters.CharactersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val marvelRepository: MarvelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20

    init {
        handleIntent(CharactersIntent.LoadCharacters)
    }

    fun handleIntent(intent: CharactersIntent) {
        when (intent) {
            is CharactersIntent.LoadCharacters -> loadCharacters()
            is CharactersIntent.RefreshCharacters -> refreshCharacters()
            is CharactersIntent.LoadMoreCharacters -> loadMoreCharacters()
            is CharactersIntent.SearchCharacters -> searchCharacters()
            is CharactersIntent.SetSearchQuery -> setQuery(intent.query)
            is CharactersIntent.ClearError -> clearError()
            is CharactersIntent.ClearLoadMoreError -> clearLoadMoreError()
            is CharactersIntent.RetryLoadMore -> retryLoadMore()
        }
    }

    private fun loadCharacters(forceRefresh: Boolean = false) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }

                val result = marvelRepository.getCharacters(
                    offset = if (forceRefresh) 0 else currentOffset,
                    limit = pageSize,
                    nameStartsWith = _uiState.value.searchQuery.takeIf { it.isNotBlank() },
                    forceRefresh = forceRefresh
                )

                val newCharacters = if (forceRefresh) {
                    result.items
                } else {
                    _uiState.value.characters + result.items
                }

                currentOffset = if (forceRefresh) result.limit else currentOffset + result.items.size

                updateState {
                    copy(
                        characters = newCharacters,
                        isLoading = false,
                        hasMore = result.hasMore,
                        isEmpty = newCharacters.isEmpty(),
                        error = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred",
                        isEmpty = characters.isEmpty()
                    )
                }
            }
        }
    }

    private fun loadMoreCharacters() {
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

                val result = marvelRepository.getCharacters(
                    offset = currentOffset,
                    limit = pageSize,
                    nameStartsWith = _uiState.value.searchQuery.takeIf { it.isNotBlank() },
                    forceRefresh = false
                )

                currentOffset += result.items.size

                updateState {
                    copy(
                        characters = characters + result.items,
                        isLoadingMore = false,
                        hasMore = result.hasMore,
                        loadMoreError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoadingMore = false,
                        loadMoreError = e.message ?: "Failed to load more characters"
                    )
                }
            }
        }
    }

    private fun searchCharacters() {
        currentOffset = 0
        loadCharacters(forceRefresh = true)
    }

    private fun setQuery(query: String) {
        val currentState = _uiState.value
        if (currentState.searchQuery == query) return

        updateState { copy(searchQuery = query) }
    }

    private fun refreshCharacters() {
        currentOffset = 0
        loadCharacters(forceRefresh = true)
    }

    private fun clearError() {
        updateState { copy(error = null) }
    }

    private fun clearLoadMoreError() {
        updateState { copy(loadMoreError = null) }
    }

    private fun retryLoadMore() {
        clearLoadMoreError()
        loadMoreCharacters()
    }

    private fun updateState(update: CharactersUiState.() -> CharactersUiState) {
        _uiState.value = _uiState.value.update()
    }
}