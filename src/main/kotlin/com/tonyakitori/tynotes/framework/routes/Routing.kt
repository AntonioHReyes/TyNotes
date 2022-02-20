package com.tonyakitori.tynotes.framework.routes

import com.tonyakitori.tynotes.framework.routes.user.createUserRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.event.Level

fun Application.configureRouting() {

    install(CallLogging) {
        level = Level.INFO
    }

    routing {

        get("/") {
            call.respondRedirect("/liveness")
        }

        get("/liveness") {
            call.respond(mapOf("status" to "Ok"))
        }

        route("/users"){ createUserRouting() }
    }
}
