package com.tonyakitori.tynotes.framework.database.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface UserProfileEntity : Entity<UserProfileEntity> {

    companion object : Entity.Factory<UserProfileEntity>()

    var userId: Int
    var profileId: Int

    val user: UserEntity?
    val profile: ProfileEntity?
}

object UserProfilesTable : Table<UserProfileEntity>("users_profiles") {

    val userId = int("user_id").bindTo { it.userId }.references(UsersTable) { it.user }
    val profileId = int("profile_id").bindTo { it.profileId }.references(ProfilesTable) { it.profile }
}