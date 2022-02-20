package com.tonyakitori.tynotes.framework.database.daos.impl

import com.tonyakitori.tynotes.framework.database.daos.ProfileDao
import com.tonyakitori.tynotes.framework.database.entities.ProfileEntity
import com.tonyakitori.tynotes.framework.database.entities.ProfilesTable
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filterColumns
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

class ProfileDaoImpl(private val database: Database): ProfileDao {

    private val profile get() = database.sequenceOf(ProfilesTable)

    override fun getProfileByKeyCode(keyCode: String): ProfileEntity? = profile
        .filterColumns { listOf(it.id, it.keycode) }
        .firstOrNull { it.keycode eq keyCode }
}