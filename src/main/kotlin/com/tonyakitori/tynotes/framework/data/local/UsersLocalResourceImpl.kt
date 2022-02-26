package com.tonyakitori.tynotes.framework.data.local

import com.tonyakitori.tynotes.data.sources.local.UsersLocalResource
import com.tonyakitori.tynotes.domain.dto.User
import com.tonyakitori.tynotes.domain.exceptions.ProfileNotFound
import com.tonyakitori.tynotes.domain.exceptions.UserIdNotGenerated
import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.framework.constants.KeyCodeConstants.USER_APP_PROFILE_KEYCODE
import com.tonyakitori.tynotes.framework.database.DatabaseManager
import com.tonyakitori.tynotes.framework.database.entities.UserEntity
import com.tonyakitori.tynotes.framework.database.entities.UserProfileEntity
import org.ktorm.entity.Tuple2
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UsersLocalResourceImpl(database: DatabaseManager) : UsersLocalResource {

    private val userDao by lazy { database.userDao }
    private val profileDao by lazy { database.profileDao }
    private val userProfileDao by lazy { database.userProfileDao }

    override fun getAllUsers(mainFilters: MainFilters): List<User> = userDao
        .getAllUsers(mainFilters)
        .map(UserEntity::toUser)

    override fun getAllUsersByPage(mainFilters: MainFilters): List<User> = userDao
        .getAllUsersByPage(mainFilters)
        .map(UserEntity::toUser)

    override fun getUserByUserNameOrEmail(userNameOrEmail: String): User? =
        userDao.getUserByUserNameOrEmail(userNameOrEmail)?.toUser()

    override fun getUserById(userId: Int): User? = userDao.getUserById(userId)?.toUser()

    override fun createUser(user: User) {
        logger.info("Create user in repo...")

        val userGeneratedId = userDao.createUser(UserEntity {
            userName = user.userName
            email = user.email
            hash = user.hash
            name = user.name
            lastName = user.lastName
            birthDate = user.birthDate
            phoneNumber = user.phoneNumber
            superUser = user.superUser
            accountLocked = user.accountLocked
            enabled = user.enabled
            credentialsExpired = user.credentialsExpired
            loginCounter = user.loginCounter
            failLoginAttempts = user.failLoginAttempts
            creationDate = user.creationDate
            lastLogin = user.lastLogin
            lastJwt = user.lastJwt
        }) ?: throw UserIdNotGenerated()

        logger.info("UserId generated: $userGeneratedId")

        val profile = profileDao.getProfileByKeyCode(USER_APP_PROFILE_KEYCODE) ?: throw ProfileNotFound()

        userProfileDao.createUserProfile(UserProfileEntity {
            userId = userGeneratedId
            profileId = profile.id
        })

        logger.info("User created successfully")
    }

    override fun updateUserById(userId: Int, user: User) = userDao.updateUserById(userId, UserEntity{
        name = user.name
        lastName = user.lastName
        birthDate = user.birthDate
        phoneNumber = user.phoneNumber
    })

    override fun deleteUserById(userId: Int) = userDao.deleteUserById(userId)
    override fun getTotalRowsAndPages(mainFilters: MainFilters): Tuple2<Int, Int>  = userDao.getTotalRowsAndPages(mainFilters)

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
}