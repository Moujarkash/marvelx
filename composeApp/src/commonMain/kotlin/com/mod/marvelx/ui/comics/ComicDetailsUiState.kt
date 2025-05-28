package com.mod.marvelx.ui.comics

import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Comic

data class ComicDetailsUiState(
    val comic: Comic? = null,
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isCharactersLoading: Boolean = false,
    val isLoadingMoreCharacters: Boolean = false,
    val hasMoreCharacters: Boolean = false,
    val error: String? = null,
    val charactersError: String? = null,
    val loadMoreCharactersError: String? = null
) {
    val isEmpty: Boolean
        get() = comic == null && !isLoading

    val isComicsEmpty: Boolean
        get() = characters.isEmpty() && !isCharactersLoading && !isLoading
}
