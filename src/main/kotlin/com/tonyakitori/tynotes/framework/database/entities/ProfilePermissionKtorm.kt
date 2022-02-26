package com.tonyakitori.tynotes.framework.database.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface ProfilePermissionEntity: Entity<ProfilePermissionEntity> {

    companion object : Entity.Factory<ProfilePermissionEntity>()

    val profileId: Int
    val permissionId: Int

}

object ProfilesPermissionsTable: Table<ProfilePermissionEntity>("profiles_permissions"){
    val profileId = int("profile_id").bindTo { it.profileId }
    val permissionId = int("permission_id").bindTo { it.permissionId }
}