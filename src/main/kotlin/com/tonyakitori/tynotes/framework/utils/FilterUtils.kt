package com.tonyakitori.tynotes.framework.utils

import com.tonyakitori.tynotes.domain.exceptions.PagePropertyNotFound
import com.tonyakitori.tynotes.domain.exceptions.SizePropertyNotFound
import com.tonyakitori.tynotes.domain.filters.PaginationFilter
import com.tonyakitori.tynotes.domain.filters.SearchFilter
import com.tonyakitori.tynotes.domain.filters.SortFilter
import com.tonyakitori.tynotes.domain.filters.SortTypes
import io.ktor.http.*

fun handlePaginationFilters(queryParams: Parameters): PaginationFilter{
    return PaginationFilter(
        page = queryParams["page"]?.toInt() ?: throw PagePropertyNotFound(),
        size = queryParams["size"]?.toInt() ?: throw SizePropertyNotFound()
    )
}

fun handleSearchFilters(queryParams: Parameters): SearchFilter?{
    var searchFilter: SearchFilter? = null
    if (queryParams.contains("search")) {
        searchFilter = SearchFilter(queryParams["search"].toString())
    }

    return searchFilter
}

fun handleSortFilters(queryParams: Parameters): SortFilter?{
    var sortFilters: SortFilter? = null
    if(queryParams.contains("sortBy")){
        sortFilters = SortFilter(
            queryParams["sortBy"].toString(),
            SortTypes.valueOf(
                if (queryParams["sortType"] != null) {
                    queryParams["sortType"].toString().uppercase()
                } else {
                    "ASC"
                }
            )
        )
    }

    return sortFilters
}