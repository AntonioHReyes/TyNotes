package com.tonyakitori.tynotes.data.sources.local

import com.tonyakitori.tynotes.domain.dto.User
import com.tonyakitori.tynotes.domain.filters.MainFilters
import org.ktorm.entity.Tuple2

interface UsersLocalResource {

    fun getAllUsers(mainFilters: MainFilters): List<User>
    fun getAllUsersByPage(mainFilters: MainFilters): List<User>
    fun getUserByUserNameOrEmail(userNameOrEmail: String): User?
    fun getUserById(userId: Int): User?
    fun createUser(user: User)
    fun updateUserById(userId: Int, user: User)
    fun deleteUserById(userId: Int)

    fun getTotalRowsAndPages(mainFilters: MainFilters): Tuple2<Int, Int>

}