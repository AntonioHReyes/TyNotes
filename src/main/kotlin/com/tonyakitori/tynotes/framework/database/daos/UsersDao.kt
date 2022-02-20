package com.tonyakitori.tynotes.framework.database.daos

import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.framework.database.entities.UserEntity
import org.ktorm.entity.Tuple2

interface UsersDao {

    fun getAllUsers(mainFilters: MainFilters): List<UserEntity>
    fun getAllUsersByPage(mainFilters: MainFilters): List<UserEntity>
    fun getUserById(userId: Int): UserEntity?
    fun getUserByUserNameOrEmail(userNameOrEmail: String): UserEntity?
    fun createUser(userEntity: UserEntity): Int?
    fun deleteUserById(userId: Int)

    fun getTotalRowsAndPages(mainFilters: MainFilters): Tuple2<Int, Int>

}