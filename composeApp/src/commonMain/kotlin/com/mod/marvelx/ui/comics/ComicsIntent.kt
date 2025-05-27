package com.mod.marvelx.ui.comics

sealed class ComicsIntent {
    data object LoadComics : ComicsIntent()
    data object RefreshComics : ComicsIntent()
    data object LoadMoreComics : ComicsIntent()
    data class SearchComics(val query: String) : ComicsIntent()
    data object ClearError : ComicsIntent()
    data object ClearLoadMoreError : ComicsIntent()
    data object RetryLoadMore : ComicsIntent()
}