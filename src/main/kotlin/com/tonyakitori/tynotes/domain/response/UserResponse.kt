package com.tonyakitori.tynotes.domain.response

data class UserResponse(
    val userId: Int?,
    val userName: String?,
    val email: String?,
    val name: String,
    val birthDate: String? = null,
    val lastName: String,
    val phoneNumber: String?,
)