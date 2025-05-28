package com.mod.marvelx.ui.characters

import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Comic

data class CharacterDetailsUiState(
    val character: Character? = null,
    val comics: List<Comic> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isComicsLoading: Boolean = false,
    val isLoadingMoreComics: Boolean = false,
    val hasMoreComics: Boolean = false,
    val error: String? = null,
    val comicsError: String? = null,
    val loadMoreComicsError: String? = null
) {
    val isEmpty: Boolean
        get() = character == null && !isLoading

    val isComicsEmpty: Boolean
        get() = comics.isEmpty() && !isComicsLoading && !isLoading
}