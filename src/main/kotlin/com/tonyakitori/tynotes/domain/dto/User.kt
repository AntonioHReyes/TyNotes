package com.tonyakitori.tynotes.domain.dto

import com.tonyakitori.tynotes.domain.response.UserResponse
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class User(
    val id: Int? = null,
    val userName: String,
    val email: String,
    val hash: String,
    val name: String,
    val lastName: String,
    val birthDate: LocalDate?,
    val phoneNumber: String?,
    val superUser: Boolean = false,
    val accountLocked: Boolean = false,
    val enabled: Boolean = true,
    val credentialsExpired: Boolean = false,
    val loginCounter: Int = 0,
    val failLoginAttempts: Int = 0,
    val creationDate: Instant = Instant.now(),
    val lastLogin: LocalDateTime = LocalDateTime.now(),
    val lastJwt: String? = ""
){
    fun toUserResponse(): UserResponse{
        return UserResponse(
            userId= this.id,
            userName = this.userName,
            email = this.email,
            name = this.name,
            lastName = this.lastName,
            phoneNumber = this.phoneNumber,
            birthDate = this.birthDate?.format(DateTimeFormatter.ISO_DATE)
        )
    }
}
