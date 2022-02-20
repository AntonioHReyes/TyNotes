package com.tonyakitori.tynotes.framework.database.daos.impl

import com.tonyakitori.tynotes.framework.database.daos.UserProfileDao
import com.tonyakitori.tynotes.framework.database.entities.UserProfileEntity
import com.tonyakitori.tynotes.framework.database.entities.UserProfilesTable
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf

class UserProfileDaoImpl(private val database: Database): UserProfileDao {

    private val userProfile get() = database.sequenceOf(UserProfilesTable)

    override fun createUserProfile(userProfileEntity: UserProfileEntity) {
        database.useTransaction {
            userProfile.add(userProfileEntity)
        }
    }
}