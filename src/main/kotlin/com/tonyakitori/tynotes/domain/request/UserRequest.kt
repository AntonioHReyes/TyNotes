package com.tonyakitori.tynotes.domain.request


data class UserRequest(
    val email: String,
    val password: String,
    val name: String,
    val lastName: String,
    val birthDate: String,
    val phoneNumber: String?
)