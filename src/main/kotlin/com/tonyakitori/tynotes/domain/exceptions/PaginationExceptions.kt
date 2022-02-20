package com.tonyakitori.tynotes.domain.exceptions

class PaginationFilterNull(message: String = "pagination filter is null"): Exception(message)
class PagePropertyNotFound(message: String = "property page for pagination not found"): Exception(message)
class SizePropertyNotFound(message: String = "property size for pagination not found"): Exception(message)