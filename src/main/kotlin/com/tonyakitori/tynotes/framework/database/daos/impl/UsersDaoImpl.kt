package com.tonyakitori.tynotes.framework.database.daos.impl

import com.tonyakitori.tynotes.domain.exceptions.PaginationFilterNull
import com.tonyakitori.tynotes.domain.exceptions.UserNotFound
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.domain.filters.SearchFilter
import com.tonyakitori.tynotes.domain.filters.SortFilter
import com.tonyakitori.tynotes.domain.filters.SortTypes
import com.tonyakitori.tynotes.framework.database.daos.UsersDao
import com.tonyakitori.tynotes.framework.database.entities.PermissionsTable
import com.tonyakitori.tynotes.framework.database.entities.ProfilesPermissionsTable
import com.tonyakitori.tynotes.framework.database.entities.UserEntity
import com.tonyakitori.tynotes.framework.database.entities.UserProfilesTable
import com.tonyakitori.tynotes.framework.database.entities.UsersTable
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.like
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.where
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
import java.time.Instant
import java.time.LocalDateTime

class UsersDaoImpl(private val database: Database) : UsersDao {

    private val users get() = database.sequenceOf(UsersTable)

    private fun getUsersWithConditions(
        searchFilter: SearchFilter?,
        onlyIds: Boolean = false
    ): EntitySequence<UserEntity, UsersTable> {

        var usersSequence = if (onlyIds) {
            users.filterColumns { listOf(it.id) }
        } else {
            users.filter { it.enabled }
        }

        if (searchFilter != null) {
            usersSequence = usersSequence.filter {
                (it.name like ("%${searchFilter.search}%"))
                    .or(it.lastName like ("%${searchFilter.search}%"))
                    .or(it.email like ("%${searchFilter.search}%"))
                    .or(it.userName like ("%${searchFilter.search}%"))
            }
        }

        return usersSequence
    }

    private fun EntitySequence<UserEntity, UsersTable>.handleSort(sortFilter: SortFilter?): EntitySequence<UserEntity, UsersTable> {
        var entitySequence = this
        entitySequence = if (sortFilter != null) {
            when (sortFilter.type) {
                SortTypes.ASC -> entitySequence.sortedBy { it[sortFilter.sort ?: "name"] }
                SortTypes.DESC -> entitySequence.sortedByDescending { it[sortFilter.sort ?: "name"] }
            }
        } else {
            val sortedSequence = entitySequence.sortedBy { it.name }
            sortedSequence
        }

        return entitySequence
    }

    override fun getAllUsers(mainFilters: MainFilters): List<UserEntity> =
        getUsersWithConditions(mainFilters.searchFilter)
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

    override fun getUserByUserNameOrEmail(userNameOrEmail: String): UserEntity? {
        val userEntity = UserEntity{}
        val listOfPermissions = arrayListOf<String>()
        database
            .from(UsersTable)
            .leftJoin(UserProfilesTable, on = UserProfilesTable.userId eq UsersTable.id)
            .leftJoin(ProfilesPermissionsTable, on = UserProfilesTable.profileId eq ProfilesPermissionsTable.profileId)
            .leftJoin(PermissionsTable, on = ProfilesPermissionsTable.permissionId eq PermissionsTable.id)
            .select(
                UsersTable.id,
                UsersTable.userName,
                UsersTable.email,
                UsersTable.hash,
                UsersTable.name,
                UsersTable.lastName,
                UsersTable.birthDate,
                UsersTable.phoneNumber,
                UsersTable.superUser,
                UsersTable.accountLocked,
                UsersTable.credentialsExpired,
                UsersTable.loginCounter,
                UsersTable.failLoginAttempts,
                UsersTable.creationDate,
                UsersTable.lastLogin,
                UsersTable.lastJwt,
                PermissionsTable.keycode,
            )
            .where(
                ((UsersTable.email eq userNameOrEmail) or
                        (UsersTable.userName eq userNameOrEmail)) and
                        (UsersTable.enabled)
            )
            .forEach { row ->
                userEntity.apply {
                    id= row[UsersTable.id]
                    userName= row[UsersTable.userName] ?: "Not found"
                    email= row[UsersTable.email] ?: "Not found"
                    hash= row[UsersTable.hash] ?: "Not found"
                    name= row[UsersTable.name] ?: "Not found"
                    lastName= row[UsersTable.lastName] ?: "Not found"
                    birthDate= row[UsersTable.birthDate]
                    phoneNumber= row[UsersTable.phoneNumber]
                    superUser= row[UsersTable.superUser] ?: false
                    accountLocked= row[UsersTable.accountLocked] ?: false
                    credentialsExpired= row[UsersTable.credentialsExpired] ?: false
                    loginCounter= row[UsersTable.loginCounter] ?: -1
                    failLoginAttempts= row[UsersTable.failLoginAttempts] ?: -1
                    creationDate= row[UsersTable.creationDate] ?: Instant.now()
                    lastLogin= row[UsersTable.lastLogin] ?: LocalDateTime.now()
                    lastJwt= row[UsersTable.lastJwt]
                }

                listOfPermissions.add(row[PermissionsTable.keycode] ?: "NOT_FOUND")
            }

        userEntity.permissions = listOfPermissions
        return userEntity
    }

    override fun createUser(userEntity: UserEntity): Int? {
        database.useTransaction {
            users.add(userEntity)
            return userEntity.id
        }
    }

    override fun updateUserById(userId: Int, user: UserEntity) {
        database.useTransaction {
            val userFound = users.find { (it.id eq userId) and (it.enabled) } ?: throw UserNotFound()
            userFound.apply {
                name = user.name
                lastName = user.lastName
                birthDate = user.birthDate
                phoneNumber = user.phoneNumber
            }

            userFound.flushChanges()
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