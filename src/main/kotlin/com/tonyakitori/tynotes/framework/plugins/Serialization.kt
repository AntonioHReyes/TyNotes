package com.tonyakitori.tynotes.framework.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
        }
    }
}
