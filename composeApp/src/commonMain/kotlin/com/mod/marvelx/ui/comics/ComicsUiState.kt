package com.mod.marvelx.ui.comics

import com.mod.marvelx.models.Comic

data class ComicsUiState(
    val comics: List<Comic> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val loadMoreError: String? = null,
    val hasMore: Boolean = true,
    val searchQuery: String = "",
    val isEmpty: Boolean = false
)
