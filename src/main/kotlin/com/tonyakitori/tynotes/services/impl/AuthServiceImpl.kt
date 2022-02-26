package com.tonyakitori.tynotes.services.impl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.tonyakitori.tynotes.data.repository.UsersRepository
import com.tonyakitori.tynotes.domain.exceptions.BadCredentials
import com.tonyakitori.tynotes.domain.request.LoginRequest
import com.tonyakitori.tynotes.domain.response.LoginResponse
import com.tonyakitori.tynotes.framework.auth.JwtConfig
import com.tonyakitori.tynotes.services.AuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthServiceImpl(private val usersRepository: UsersRepository, private val jwtConfig: JwtConfig): AuthService {

    override fun login(loginRequest: LoginRequest): LoginResponse {
        logger.info("Start login...")
        val (username, password) = loginRequest
        val user = usersRepository.getUserByUserNameOrEmail(username) ?: throw BadCredentials()

        logger.info("User found in db: ${user.userName}")

        val result = BCrypt.verifyer().verify(password.toCharArray(), user.hash)

        if(!result.verified) throw BadCredentials()

        logger.info("Password verification success...")

        val token = jwtConfig.generateToken(JwtConfig.JwtUser(
            userId = (user.id ?: -1).toString(),
            userName = user.userName,
            roles = user.roles.toSet()
        ))

        return LoginResponse(
            userName = user.userName,
            email = user.email,
            name = user.name,
            lastName = user.lastName,
            token = token
        )
    }

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
}