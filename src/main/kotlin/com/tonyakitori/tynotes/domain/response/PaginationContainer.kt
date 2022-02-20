package com.tonyakitori.tynotes.domain.response

data class PaginationContainer<T>(
    val content: T,
    val size: Int,
    val page: Int,
    val totalPages: Int,
    val totalElements: Int
)