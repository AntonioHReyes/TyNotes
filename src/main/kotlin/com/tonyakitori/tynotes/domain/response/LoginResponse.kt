package com.tonyakitori.tynotes.domain.response

data class LoginResponse(
    val userName: String?,
    val email: String?,
    val name: String,
    val lastName: String,
    val token: String,
)