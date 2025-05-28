package com.mod.marvelx.ui.comics

sealed interface ComicDetailsIntent {
    data object LoadComicDetails : ComicDetailsIntent
    data object RefreshComicDetails : ComicDetailsIntent
    data object LoadMoreCharacters : ComicDetailsIntent
    data object RefreshCharacters : ComicDetailsIntent
    data object ClearError : ComicDetailsIntent
    data object ClearCharactersError : ComicDetailsIntent
    data object ClearLoadMoreCharactersError : ComicDetailsIntent
    data object RetryLoadMoreCharacters : ComicDetailsIntent
}