package com.tonyakitori.tynotes.framework.plugins

import com.tonyakitori.tynotes.framework.auth.JwtConfig
import com.tonyakitori.tynotes.framework.auth.RoleBasedAuthorization
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val jwtConfig: JwtConfig by inject()

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    install(RoleBasedAuthorization) {
        getRoles { (it as JwtConfig.JwtUser).roles }
    }

}
