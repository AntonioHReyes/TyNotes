package com.tonyakitori.tynotes.framework.database.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface PermissionEntity: Entity<PermissionEntity> {

    companion object : Entity.Factory<PermissionEntity>()

    val id: Int
    val keycode: String
    val description: String

}

object PermissionsTable: Table<PermissionEntity>("permissions"){

    val id = int("id").primaryKey().bindTo { it.id }
    val keycode = varchar("keycode").bindTo { it.keycode }
    val description = varchar("description").bindTo { it.description }
}