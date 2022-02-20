package com.tonyakitori.tynotes.framework.database.daos

import com.tonyakitori.tynotes.framework.database.entities.ProfileEntity

interface ProfileDao {
    fun getProfileByKeyCode(keyCode: String): ProfileEntity?
}