package com.tonyakitori.tynotes.services

import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.request.UserRequest
import com.tonyakitori.tynotes.domain.request.UserRequestUpdate
import com.tonyakitori.tynotes.domain.response.PaginationContainer
import com.tonyakitori.tynotes.domain.response.UserResponse

interface UserService {
    fun getAllUsers(mainFilters: MainFilters): List<UserResponse>
    fun getAllUsersByPage(mainFilters: MainFilters): PaginationContainer<List<UserResponse>>
    fun createUser(user: UserRequest)
    fun getUserById(userId: Int?): UserResponse
    fun updateUserById(userId: Int?, userRequestUpdate: UserRequestUpdate)
    fun deleteUserById(userId: Int?)
}