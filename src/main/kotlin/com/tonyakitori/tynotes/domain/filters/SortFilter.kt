package com.tonyakitori.tynotes.domain.filters

enum class SortTypes {
    ASC, DESC
}

class SortFilter(
    val sort: String?,
    val type: SortTypes = SortTypes.ASC
)