package com.tonyakitori.tynotes.data.repository.impl

import com.tonyakitori.tynotes.data.repository.UsersRepository
import com.tonyakitori.tynotes.data.sources.local.UsersLocalResource
import com.tonyakitori.tynotes.domain.dto.User
import com.tonyakitori.tynotes.domain.exceptions.PaginationFilterNull
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.response.PaginationContainer
import com.tonyakitori.tynotes.domain.response.UserResponse

class UsersRepositoryImpl(private val usersLocalSource: UsersLocalResource): UsersRepository {

    override fun getAllUsers(mainFilters: MainFilters): List<User> = usersLocalSource.getAllUsers(mainFilters)
    override fun getAllUsersByPage(mainFilters: MainFilters): PaginationContainer<List<UserResponse>> {
       val (totalRows, totalPages) = usersLocalSource.getTotalRowsAndPages(mainFilters)
        if(mainFilters.paginationFilter == null){
            throw PaginationFilterNull()
        }

        return PaginationContainer(
            content = usersLocalSource.getAllUsersByPage(mainFilters).map(User::toUserResponse),
            size = mainFilters.paginationFilter.size,
            page = mainFilters.paginationFilter.page,
            totalPages = totalPages,
            totalElements = totalRows
        )
    }
    override fun getUserById(userId: Int): User? = usersLocalSource.getUserById(userId)
    override fun getUserByUserNameOrEmail(userNameOrEmail: String): User? = usersLocalSource.getUserByUserNameOrEmail(userNameOrEmail)
    override fun createUser(user: User) = usersLocalSource.createUser(user)
    override fun updateUserById(userId: Int, user: User) = usersLocalSource.updateUserById(userId, user)
    override fun deleteUserById(userId: Int) = usersLocalSource.deleteUserById(userId)
}