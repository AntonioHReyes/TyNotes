package com.tonyakitori.tynotes

import com.tonyakitori.tynotes.framework.plugins.configureSerialization
import com.tonyakitori.tynotes.framework.plugins.startKoin
import com.tonyakitori.tynotes.framework.routes.configureRouting
import io.ktor.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    configureRouting()
    configureSerialization()
    startKoin()
}