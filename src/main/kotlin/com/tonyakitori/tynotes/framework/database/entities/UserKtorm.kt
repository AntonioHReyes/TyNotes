package com.tonyakitori.tynotes.framework.database.entities

import com.tonyakitori.tynotes.domain.dto.User
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.date
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime


interface UserEntity: Entity<UserEntity> {

    companion object : Entity.Factory<UserEntity>()

    val id: Int?
    var userName: String
    var email: String
    var hash: String
    var name: String
    var lastName: String
    var birthDate: LocalDate?
    var phoneNumber: String?
    var superUser: Boolean
    var accountLocked: Boolean
    var enabled: Boolean
    var credentialsExpired: Boolean
    var loginCounter: Int
    var failLoginAttempts: Int
    var creationDate: Instant
    var lastLogin: LocalDateTime
    var lastJwt: String?

    fun toUser(): User {
        return User(
            id = this.id,
            userName= this.userName,
            email= this.email,
            hash= this.hash,
            name= this.name,
            lastName= this.lastName,
            birthDate= this.birthDate,
            phoneNumber= this.phoneNumber,
            superUser= this.superUser,
            accountLocked= this.accountLocked,
            enabled= this.enabled,
            credentialsExpired= this.credentialsExpired,
            loginCounter= this.loginCounter,
            failLoginAttempts= this.failLoginAttempts,
            creationDate= this.creationDate,
            lastLogin= this.lastLogin,
            lastJwt= this.lastJwt
        )
    }
}

object UsersTable: Table<UserEntity>("users"){

    val id = int("id").primaryKey().bindTo { it.id }
    val userName = varchar("username").bindTo { it.userName }
    val email = varchar("email").bindTo { it.email }
    val hash = varchar("hash").bindTo { it.hash }
    val name = varchar("name").bindTo { it.name }
    val lastName = varchar("last_name").bindTo { it.lastName }
    val birthDate = date("birthdate").bindTo { it.birthDate }
    val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
    val superUser = boolean("superuser").bindTo { it.superUser }
    val accountLocked = boolean("account_locked").bindTo { it.accountLocked }
    val enabled = boolean("enabled").bindTo { it.enabled }
    val credentialsExpired = boolean("credentials_expired").bindTo { it.credentialsExpired }
    val loginCounter = int("login_counter").bindTo { it.loginCounter }
    val failLoginAttempts = int("failed_login_attempts").bindTo { it.failLoginAttempts }
    val creationDate = timestamp("creation_date").bindTo { it.creationDate }
    val lastLogin = datetime("last_login_time").bindTo { it.lastLogin }
    val lastJwt = varchar("last_jwt").bindTo { it.lastJwt }


}

