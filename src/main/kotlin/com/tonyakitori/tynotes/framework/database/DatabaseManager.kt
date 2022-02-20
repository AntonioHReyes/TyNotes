package com.tonyakitori.tynotes.framework.database

import com.tonyakitori.tynotes.framework.database.daos.ProfileDao
import com.tonyakitori.tynotes.framework.database.daos.UserProfileDao
import com.tonyakitori.tynotes.framework.database.daos.UsersDao
import com.tonyakitori.tynotes.framework.database.daos.impl.ProfileDaoImpl
import com.tonyakitori.tynotes.framework.database.daos.impl.UserProfileDaoImpl
import com.tonyakitori.tynotes.framework.database.daos.impl.UsersDaoImpl
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import org.ktorm.database.Database

class DatabaseManager {

    private val hostName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.database.db_hostname").getString()
    private val databaseName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.database.db_name").getString()
    private val userName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.database.db_username").getString()
    private val password = HoconApplicationConfig(ConfigFactory.load()).property("ktor.database.db_password").getString()

    //database
    private val kTormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostName:3306/$databaseName?user=$userName&password=$password&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false"
        kTormDatabase = Database.connect(jdbcUrl)
    }

    val userDao: UsersDao get() = UsersDaoImpl(kTormDatabase)
    val profileDao: ProfileDao get() = ProfileDaoImpl(kTormDatabase)
    val userProfileDao: UserProfileDao get() = UserProfileDaoImpl(kTormDatabase)

}