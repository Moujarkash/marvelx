package com.mod.marvelx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mod.marvelx.repositories.MarvelRepository
import com.mod.marvelx.ui.characters.CharacterDetailsIntent
import com.mod.marvelx.ui.characters.CharacterDetailsUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val marvelRepository: MarvelRepository,
    private val characterId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    private var currentComicsOffset = 0
    private val comicsPageSize = 20

    init {
        handleIntent(CharacterDetailsIntent.LoadCharacterDetails)
    }

    fun handleIntent(intent: CharacterDetailsIntent) {
        when (intent) {
            is CharacterDetailsIntent.LoadCharacterDetails -> loadCharacterDetails()
            is CharacterDetailsIntent.RefreshCharacterDetails -> refreshCharacterDetails()
            is CharacterDetailsIntent.LoadMoreComics -> loadMoreComics()
            is CharacterDetailsIntent.RefreshComics -> refreshComics()
            is CharacterDetailsIntent.ClearError -> clearError()
            is CharacterDetailsIntent.ClearComicsError -> clearComicsError()
            is CharacterDetailsIntent.ClearLoadMoreComicsError -> clearLoadMoreComicsError()
            is CharacterDetailsIntent.RetryLoadMoreComics -> retryLoadMoreComics()
        }
    }

    private fun loadCharacterDetails() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }

                // Load character details and comics concurrently
                val characterDeferred = async { marvelRepository.getCharacterById(characterId) }
                val comicsDeferred = async {
                    marvelRepository.getCharacterComics(
                        characterId = characterId,
                        offset = 0,
                        limit = comicsPageSize
                    )
                }

                val character = characterDeferred.await()
                val comicsResult = comicsDeferred.await()

                currentComicsOffset = comicsResult.items.size

                updateState {
                    copy(
                        character = character,
                        comics = comicsResult.items,
                        isLoading = false,
                        isComicsLoading = false,
                        hasMoreComics = comicsResult.hasMore,
                        error = null,
                        comicsError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load character details"
                    )
                }
            }
        }
    }

    private fun refreshCharacterDetails() {
        currentComicsOffset = 0
        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isRefreshing = true,
                        error = null,
                        comicsError = null
                    )
                }

                // Refresh character details and comics concurrently
                val characterDeferred = async {
                    marvelRepository.getCharacterById(characterId, forceRefresh = true)
                }
                val comicsDeferred = async {
                    marvelRepository.getCharacterComics(
                        characterId = characterId,
                        offset = 0,
                        limit = comicsPageSize,
                        forceRefresh = true
                    )
                }

                val character = characterDeferred.await()
                val comicsResult = comicsDeferred.await()

                currentComicsOffset = comicsResult.items.size

                updateState {
                    copy(
                        character = character,
                        comics = comicsResult.items,
                        isRefreshing = false,
                        isComicsLoading = false,
                        hasMoreComics = comicsResult.hasMore,
                        error = null,
                        comicsError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isRefreshing = false,
                        error = e.message ?: "Failed to refresh character details"
                    )
                }
            }
        }
    }

    private fun loadMoreComics() {
        val currentState = _uiState.value
        if (currentState.isLoadingMoreComics || !currentState.hasMoreComics ||
            currentState.isLoading || currentState.isComicsLoading) return

        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isLoadingMoreComics = true,
                        loadMoreComicsError = null
                    )
                }

                val result = marvelRepository.getCharacterComics(
                    characterId = characterId,
                    offset = currentComicsOffset,
                    limit = comicsPageSize,
                    forceRefresh = false
                )

                currentComicsOffset += result.items.size

                updateState {
                    copy(
                        comics = comics + result.items,
                        isLoadingMoreComics = false,
                        hasMoreComics = result.hasMore,
                        loadMoreComicsError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoadingMoreComics = false,
                        loadMoreComicsError = e.message ?: "Failed to load more comics"
                    )
                }
            }
        }
    }

    private fun refreshComics() {
        currentComicsOffset = 0
        viewModelScope.launch {
            try {
                updateState {
                    copy(
                        isComicsLoading = true,
                        comicsError = null
                    )
                }

                val result = marvelRepository.getCharacterComics(
                    characterId = characterId,
                    offset = 0,
                    limit = comicsPageSize,
                    forceRefresh = true
                )

                currentComicsOffset = result.items.size

                updateState {
                    copy(
                        comics = result.items,
                        isComicsLoading = false,
                        hasMoreComics = result.hasMore,
                        comicsError = null
                    )
                }

            } catch (e: Exception) {
                updateState {
                    copy(
                        isComicsLoading = false,
                        comicsError = e.message ?: "Failed to refresh comics"
                    )
                }
            }
        }
    }

    private fun clearError() {
        updateState { copy(error = null) }
    }

    private fun clearComicsError() {
        updateState { copy(comicsError = null) }
    }

    private fun clearLoadMoreComicsError() {
        updateState { copy(loadMoreComicsError = null) }
    }

    private fun retryLoadMoreComics() {
        clearLoadMoreComicsError()
        loadMoreComics()
    }

    private fun updateState(update: CharacterDetailsUiState.() -> CharacterDetailsUiState) {
        _uiState.value = _uiState.value.update()
    }
}