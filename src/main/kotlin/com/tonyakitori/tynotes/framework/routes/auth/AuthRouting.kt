package com.tonyakitori.tynotes.framework.routes.auth

import com.tonyakitori.tynotes.domain.request.LoginRequest
import com.tonyakitori.tynotes.framework.utils.handleAuthExceptions
import com.tonyakitori.tynotes.services.AuthService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.createAuthRouting(){

    val authService: AuthService by inject()

    post {
        try{
            val body = call.receive<LoginRequest>()
            call.respond(authService.login(loginRequest= body))
        }catch (e: Exception){
            e.printStackTrace()
            call.application.log.error("Error in login: ${e.message}")
            handleAuthExceptions(e)
        }
    }

}