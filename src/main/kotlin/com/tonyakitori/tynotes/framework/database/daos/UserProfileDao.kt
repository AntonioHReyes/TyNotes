package com.tonyakitori.tynotes.framework.database.daos

import com.tonyakitori.tynotes.framework.database.entities.UserProfileEntity

interface UserProfileDao {
    fun createUserProfile(userProfileEntity: UserProfileEntity)
}