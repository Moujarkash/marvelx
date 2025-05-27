package com.mod.marvelx.ui.characters

sealed class CharactersIntent {
    data object LoadCharacters : CharactersIntent()
    data object RefreshCharacters : CharactersIntent()
    data object LoadMoreCharacters : CharactersIntent()
    data object SearchCharacters : CharactersIntent()
    data class SetSearchQuery(val query: String) : CharactersIntent()
    data object ClearError : CharactersIntent()
    data object ClearLoadMoreError : CharactersIntent()
    data object RetryLoadMore : CharactersIntent()
}