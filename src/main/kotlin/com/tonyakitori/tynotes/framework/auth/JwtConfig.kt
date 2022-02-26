package com.tonyakitori.tynotes.framework.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.tonyakitori.tynotes.framework.auth.JwtConfig.Constants.CLAIM_USERID
import com.tonyakitori.tynotes.framework.auth.JwtConfig.Constants.CLAIM_USERNAME
import com.tonyakitori.tynotes.framework.auth.JwtConfig.Constants.jwtAudience
import com.tonyakitori.tynotes.framework.auth.JwtConfig.Constants.jwtIssuer
import com.tonyakitori.tynotes.framework.auth.JwtConfig.Constants.jwtSecret
import com.typesafe.config.ConfigFactory
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import java.util.*

class JwtConfig {

    companion object Constants {
        // jwt config
        private val jwtSecret = HoconApplicationConfig(ConfigFactory.load()).property("ktor.jwt.secret").getString()
        private val jwtIssuer = HoconApplicationConfig(ConfigFactory.load()).property("ktor.jwt.issuer").getString()
        private val jwtAudience = HoconApplicationConfig(ConfigFactory.load()).property("ktor.jwt.audience").getString()
        private val jwtRealm = HoconApplicationConfig(ConfigFactory.load()).property("ktor.jwt.realm").getString()

        // claims
        private const val CLAIM_USERID = "userId"
        private const val CLAIM_USERNAME = "userName"
        private const val CLAIM_ROLES = "roles"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .withClaim(CLAIM_ROLES, user.roles.toList())
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userId = it.payload.getClaim(CLAIM_USERID).asString()
            val userName = it.payload.getClaim(CLAIM_USERNAME).asString()
            val roles = it.payload.getClaim(CLAIM_ROLES).asArray(String::class.java).toSet()

            if (userId != null && userName != null) {
                JwtUser(userId, userName, roles)
            } else {
                null
            }
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(
        val userId: String,
        val userName: String,
        val roles: Set<String> = setOf()
        ): Principal


}