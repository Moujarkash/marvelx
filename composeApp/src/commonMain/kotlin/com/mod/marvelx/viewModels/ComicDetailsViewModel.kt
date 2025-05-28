package com.mod.marvelx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mod.marvelx.repositories.MarvelRepository
import com.mod.marvelx.ui.comics.ComicDetailsIntent
import com.mod.marvelx.ui.comics.ComicDetailsUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComicDetailsViewModel(
    private val marvelRepository: MarvelRepository,
    private val comicId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComicDetailsUiState())
    val uiState: StateFlow<ComicDetailsUiState> = _uiState.asStateFlow()

    private var currentCharactersOffset = 0
    private val charactersPageSize = 20

    init {
        handleIntent(ComicDetailsIntent.LoadComicDetails)
    }

    fun handleIntent(intent: ComicDetailsIntent) {
        when (intent) {
            is ComicDetailsIntent.LoadComicDetails -> loadComicDetails()
            is ComicDetailsIntent.RefreshComicDetails -> refreshComicDetails()
            is ComicDetailsIntent.LoadMoreCharacters -> loadMoreCharacters()
            is ComicDetailsIntent.RefreshCharacters -> refreshCharacters()
            is ComicDetailsIntent.ClearError -> clearError()
            is ComicDetailsIntent.ClearCharactersError -> clearCharactersError()
            is ComicDetailsIntent.ClearLoadMoreCharactersError -> clearLoadMoreCharactersError()
            is ComicDetailsIntent.RetryLoadMoreCharacters -> retryLoadMoreCharacters()
        }
    }

    private fun loadComicDetails() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }

                // Load comic details and characters concurrently
                val comicDeferred = async { marvelRepository.getComicById(comicId) }
                val charactersDeferred = async {
                    marvelRepository.getComicCharacters(
                        comicId = comicId,
                        offset = 0,
                        limit = charactersPageSize
                    )
                }

                val comic = comicDeferred.await()
                val charactersResult = charactersDeferred.await()

                currentCharactersOffset = charactersResult.items.size

                updateState {
                    copy(
                        comic = comic,
                        characters = charactersResult.items,
                        isLoading = false,
                        isCharactersLoading = false,
                        hasMoreCharacters = charactersResult.hasMore,
                        error = null,
                        charactersError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load comic details"
                    )
                }
            }
        }
    }

    private fun refreshComicDetails() {
        currentCharactersOffset = 0
        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isRefreshing = true,
                        error = null,
                        charactersError = null
                    )
                }

                // Refresh comic details and characters concurrently
                val comicDeferred = async {
                    marvelRepository.getComicById(comicId, forceRefresh = true)
                }
                val charactersDeferred = async {
                    marvelRepository.getComicCharacters(
                        comicId = comicId,
                        offset = 0,
                        limit = charactersPageSize,
                        forceRefresh = true
                    )
                }

                val comic = comicDeferred.await()
                val charactersResult = charactersDeferred.await()

                currentCharactersOffset = charactersResult.items.size

                updateState {
                    copy(
                        comic = comic,
                        characters = charactersResult.items,
                        isRefreshing = false,
                        isCharactersLoading = false,
                        hasMoreCharacters = charactersResult.hasMore,
                        error = null,
                        charactersError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isRefreshing = false,
                        error = e.message ?: "Failed to refresh comic details"
                    )
                }
            }
        }
    }

    private fun loadMoreCharacters() {
        val currentState = _uiState.value
        if (currentState.isLoadingMoreCharacters || !currentState.hasMoreCharacters ||
            currentState.isLoading || currentState.isCharactersLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoadingMoreCharacters = true,
                        loadMoreCharactersError = null
                    )
                }

                val result = marvelRepository.getComicCharacters(
                    comicId = comicId,
                    offset = currentCharactersOffset,
                    limit = charactersPageSize,
                    forceRefresh = false
                )

                currentCharactersOffset += result.items.size

                updateState {
                    copy(
                        characters = characters + result.items,
                        isLoadingMoreCharacters = false,
                        hasMoreCharacters = result.hasMore,
                        loadMoreCharactersError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoadingMoreCharacters = false,
                        loadMoreCharactersError = e.message ?: "Failed to load more characters"
                    )
                }
            }
        }
    }

    private fun refreshCharacters() {
        currentCharactersOffset = 0
        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isCharactersLoading = true,
                        charactersError = null
                    )
                }

                val result = marvelRepository.getComicCharacters(
                    comicId = comicId,
                    offset = 0,
                    limit = charactersPageSize,
                    forceRefresh = true
                )

                currentCharactersOffset = result.items.size

                updateState {
                    copy(
                        characters = result.items,
                        isCharactersLoading = false,
                        hasMoreCharacters = result.hasMore,
                        charactersError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isCharactersLoading = false,
                        charactersError = e.message ?: "Failed to refresh characters"
                    )
                }
            }
        }
    }

    private fun clearError() {
        updateState { copy(error = null) }
    }

    private fun clearCharactersError() {
        updateState { copy(charactersError = null) }
    }

    private fun clearLoadMoreCharactersError() {
        updateState { copy(loadMoreCharactersError = null) }
    }

    private fun retryLoadMoreCharacters() {
        clearLoadMoreCharactersError()
        loadMoreCharacters()
    }

    private fun updateState(update: ComicDetailsUiState.() -> ComicDetailsUiState) {
        _uiState.value = _uiState.value.update()
    }
}