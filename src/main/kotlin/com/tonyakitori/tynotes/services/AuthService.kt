package com.tonyakitori.tynotes.services

import com.tonyakitori.tynotes.domain.request.LoginRequest
import com.tonyakitori.tynotes.domain.response.LoginResponse

interface AuthService {

    fun login(loginRequest: LoginRequest): LoginResponse

}