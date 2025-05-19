package com.mod.marvelx.models

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int,
    val status: String,
    val data: DataResponse<T>? = null
)

@Serializable
data class DataResponse<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
)
