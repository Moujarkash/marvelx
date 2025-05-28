package com.mod.marvelx.ui.characters

sealed interface CharacterDetailsIntent {
    data object LoadCharacterDetails : CharacterDetailsIntent
    data object RefreshCharacterDetails : CharacterDetailsIntent
    data object LoadMoreComics : CharacterDetailsIntent
    data object RefreshComics : CharacterDetailsIntent
    data object ClearError : CharacterDetailsIntent
    data object ClearComicsError : CharacterDetailsIntent
    data object ClearLoadMoreComicsError : CharacterDetailsIntent
    data object RetryLoadMoreComics : CharacterDetailsIntent
}