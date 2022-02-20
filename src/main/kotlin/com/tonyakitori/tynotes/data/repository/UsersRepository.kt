package com.tonyakitori.tynotes.data.repository

import com.tonyakitori.tynotes.domain.dto.User
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.response.PaginationContainer
import com.tonyakitori.tynotes.domain.response.UserResponse

interface UsersRepository {

    fun getAllUsers(mainFilters: MainFilters): List<User>
    fun getAllUsersByPage(mainFilters: MainFilters): PaginationContainer<List<UserResponse>>
    fun getUserById(userId: Int): User?
    fun getUserByUserNameOrEmail(userNameOrEmail: String): User?
    fun createUser(user: User)
    fun deleteUserById(userId: Int)

}