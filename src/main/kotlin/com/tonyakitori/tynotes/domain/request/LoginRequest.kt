package com.tonyakitori.tynotes.domain.request

data class LoginRequest(
    val identifier: String,
    val password: String
)