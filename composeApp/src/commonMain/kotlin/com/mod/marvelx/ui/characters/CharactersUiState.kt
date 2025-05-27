package com.mod.marvelx.ui.characters

import com.mod.marvelx.models.Character

data class CharactersUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val loadMoreError: String? = null,
    val hasMore: Boolean = true,
    val searchQuery: String = "",
    val isEmpty: Boolean = false
)