package com.tonyakitori.tynotes.framework.database.daos.impl

import com.tonyakitori.tynotes.domain.exceptions.PaginationFilterNull
import com.tonyakitori.tynotes.domain.exceptions.UserNotFound
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.filters.SearchFilter
import com.tonyakitori.tynotes.domain.filters.SortFilter
import com.tonyakitori.tynotes.domain.filters.SortTypes
import com.tonyakitori.tynotes.framework.database.daos.UsersDao
import com.tonyakitori.tynotes.framework.database.entities.UserEntity
import com.tonyakitori.tynotes.framework.database.entities.UsersTable
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.dsl.or
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.Tuple2
import org.ktorm.entity.add
import org.ktorm.entity.drop
import org.ktorm.entity.filter
import org.ktorm.entity.filterColumns
import org.ktorm.entity.find
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.sortedBy
import org.ktorm.entity.sortedByDescending
import org.ktorm.entity.take
import org.ktorm.entity.toList
import org.ktorm.entity.tupleOf

class UsersDaoImpl(private val database: Database) : UsersDao {

    private val users get() = database.sequenceOf(UsersTable)

    private fun getUsersWithConditions(searchFilter: SearchFilter?, onlyIds: Boolean = false): EntitySequence<UserEntity, UsersTable> {

        var usersSequence = if(onlyIds){users.filterColumns { listOf(it.id) }} else {users.filter { it.enabled }}

        if (searchFilter != null) {
            usersSequence = usersSequence.filter {
                (it.name like("%${searchFilter.search}%"))
                    .or(it.lastName like("%${searchFilter.search}%"))
                    .or(it.email like("%${searchFilter.search}%"))
                    .or(it.userName like("%${searchFilter.search}%"))
            }
        }

        return usersSequence
    }

    private fun EntitySequence<UserEntity, UsersTable>.handleSort(sortFilter: SortFilter?): EntitySequence<UserEntity, UsersTable>{
        var entitySequence = this
        entitySequence = if(sortFilter != null){
            when(sortFilter.type){
                SortTypes.ASC -> entitySequence.sortedBy { it[sortFilter.sort ?: "name"] }
                SortTypes.DESC -> entitySequence.sortedByDescending { it[sortFilter.sort ?: "name"] }
            }
        }else{
            val sortedSequence = entitySequence.sortedBy { it.name }
            sortedSequence
        }

        return entitySequence
    }

    override fun getAllUsers(mainFilters: MainFilters): List<UserEntity> = getUsersWithConditions(mainFilters.searchFilter)
        .handleSort(mainFilters.sortFilter)
        .toList()

    override fun getAllUsersByPage(mainFilters: MainFilters): List<UserEntity> {
        val pageFilters = mainFilters.paginationFilter ?: throw PaginationFilterNull()
        val searchFilter = mainFilters.searchFilter
        val sortFilter = mainFilters.sortFilter

        return getUsersWithConditions(searchFilter)
            .drop(
                when (pageFilters.page) {
                    0 -> 0
                    else -> (pageFilters.page - 1) + pageFilters.size
                }
            )
            .take(pageFilters.size)
            .handleSort(sortFilter)
            .toList()
    }

    override fun getUserById(userId: Int): UserEntity? = users.firstOrNull {
        (it.id eq userId) and (it.enabled eq true)
    }

    override fun getUserByUserNameOrEmail(userNameOrEmail: String): UserEntity? =
        users.firstOrNull { (it.userName eq userNameOrEmail) or (it.email eq userNameOrEmail) }

    override fun createUser(userEntity: UserEntity): Int? {
        database.useTransaction {
            users.add(userEntity)
            return userEntity.id
        }
    }

    override fun deleteUserById(userId: Int) {
        database.useTransaction {
            val user = users.find { it.id eq userId } ?: throw UserNotFound()
            user.enabled = false
            user.flushChanges()
        }
    }

    override fun getTotalRowsAndPages(mainFilters: MainFilters): Tuple2<Int, Int> {
        val totalRecords = getUsersWithConditions(mainFilters.searchFilter, true).totalRecords
        val totalPages = totalRecords.div(mainFilters.paginationFilter?.size ?: 0)
        return tupleOf(totalRecords, totalPages)
    }
}