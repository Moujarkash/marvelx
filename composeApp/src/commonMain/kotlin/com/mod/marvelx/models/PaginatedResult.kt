package com.mod.marvelx.models

data class PaginatedResult<T>(
    val items: List<T>,
    val offset: Int,
    val limit: Int,
    val total: Int,
    val hasMore: Boolean
)
