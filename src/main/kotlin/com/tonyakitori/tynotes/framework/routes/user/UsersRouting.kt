package com.tonyakitori.tynotes.framework.routes.user

import com.tonyakitori.tynotes.domain.filters.MainFilters
import com.tonyakitori.tynotes.framework.utils.handlePaginationFilters
import com.tonyakitori.tynotes.framework.utils.handleSearchFilters
import com.tonyakitori.tynotes.framework.utils.handleSortFilters
import com.tonyakitori.tynotes.framework.utils.handleUserExceptions
import com.tonyakitori.tynotes.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.createUserRouting() {

    val userService: UserService by inject()

    get {
        val queryParams = call.request.queryParameters
        try {
            call.respond(
                HttpStatusCode.OK,
                when {
                    queryParams.contains("pagination") and (queryParams["pagination"].toBoolean()) -> {
                        userService.getAllUsersByPage(
                            MainFilters(
                                handlePaginationFilters(queryParams),
                                handleSearchFilters(queryParams),
                                handleSortFilters(queryParams)
                            )
                        )
                    }
                    else -> {
                        userService.getAllUsers(MainFilters(
                            searchFilter = handleSearchFilters(queryParams),
                            sortFilter = handleSortFilters(queryParams)
                        ))
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            call.application.log.error("Error in create user: ${e.message}")
            handleUserExceptions(e)
        }
    }

    post {
        call.application.log.info("Create user")
        try {
            userService.createUser(call.receive())
            call.respond(HttpStatusCode.OK, mapOf("message" to "Ok"))
        } catch (e: Exception) {
            e.printStackTrace()
            call.application.log.error("Error in create user: ${e.message}")
            handleUserExceptions(e)
        }
    }

    get("/{userId}") {
        call.application.log.info("Get User by id....")
        val userId = call.parameters["userId"]?.toIntOrNull()
        try {
            val user = userService.getUserById(userId)
            call.respond(HttpStatusCode.OK, user)
        } catch (e: Exception) {
            e.printStackTrace()
            call.application.log.error("Error in get user: ${e.message}")
            handleUserExceptions(e)
        }
    }

    put("/{userId}"){
        call.application.log.info("Update user by Id...")
        val userId = call.parameters["userId"]?.toIntOrNull()
        try {
            userService.updateUserById(userId, call.receive())
            call.respond(HttpStatusCode.OK, mapOf("message" to "Ok"))
        }catch (e: Exception){
            e.printStackTrace()
            call.application.log.error("Error in update user: ${e.message}")
            handleUserExceptions(e)
        }
    }

    delete("/{userId}") {
        call.application.log.info("Delete User by id....")
        val userId = call.parameters["userId"]?.toIntOrNull()
        try {
            userService.deleteUserById(userId)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Ok"))
        } catch (e: Exception) {
            e.printStackTrace()
            call.application.log.error("Error in delete: ${e.message}")
            handleUserExceptions(e)
        }
    }

}