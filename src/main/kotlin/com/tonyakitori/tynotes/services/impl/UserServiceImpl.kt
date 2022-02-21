package com.tonyakitori.tynotes.services.impl

import com.tonyakitori.tynotes.data.repository.UsersRepository
import com.tonyakitori.tynotes.domain.dto.User
import com.tonyakitori.tynotes.domain.exceptions.UserFoundButDisabled
import com.tonyakitori.tynotes.domain.exceptions.UserIdNotValid
import com.tonyakitori.tynotes.domain.exceptions.UserNameOrEmailExist
import com.tonyakitori.tynotes.domain.exceptions.UserNotFound
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.request.UserRequest
import com.tonyakitori.tynotes.domain.request.UserRequestUpdate
import com.tonyakitori.tynotes.domain.response.PaginationContainer
import com.tonyakitori.tynotes.domain.response.UserResponse
import com.tonyakitori.tynotes.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserServiceImpl(private val userRepo: UsersRepository) : UserService {

    override fun getAllUsers(mainFilters: MainFilters): List<UserResponse> = userRepo.getAllUsers(mainFilters).map(User::toUserResponse)

    override fun getAllUsersByPage(mainFilters: MainFilters): PaginationContainer<List<UserResponse>> = userRepo.getAllUsersByPage(mainFilters)

    override fun createUser(user: UserRequest) {
        logger.info("Start user creation")

        userRepo.getUserByUserNameOrEmail(user.email)?.let {
            logger.info("User found in db")
            if(it.enabled){
                throw UserNameOrEmailExist()
            }else{
                throw UserFoundButDisabled()
            }
        }

        logger.info("User not found, start creation")

        val userDao = User(
            userName= user.email,
            email= user.email,
            hash= user.password,
            name= user.name,
            lastName= user.lastName,
            birthDate= LocalDate.parse(user.birthDate, DateTimeFormatter.ISO_DATE),
            phoneNumber= user.phoneNumber,
        )

        userRepo.createUser(userDao)
    }

    override fun getUserById(userId: Int?): UserResponse {
        logger.info("Get User by id: $userId")

        if(userId == null){
            throw UserIdNotValid()
        }

        val user = userRepo.getUserById(userId) ?: throw UserNotFound()

        return user.toUserResponse()
    }

    override fun updateUserById(userId: Int?, userRequestUpdate: UserRequestUpdate) {
        logger.info("Update user by id: $userId")

        if(userId == null){
            throw UserIdNotValid()
        }

        userRepo.updateUserById(userId, User(
            name = userRequestUpdate.name,
            lastName = userRequestUpdate.lastName,
            birthDate = LocalDate.parse(userRequestUpdate.birthDate, DateTimeFormatter.ISO_DATE),
            phoneNumber = userRequestUpdate.phoneNumber,
        ))
    }

    override fun deleteUserById(userId: Int?) {
        logger.info("Delete User by id: $userId")

        if(userId == null){
            throw UserIdNotValid()
        }

        userRepo.deleteUserById(userId)
    }


    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
}