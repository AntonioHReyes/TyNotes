package com.tonyakitori.tynotes.framework.plugins

import com.tonyakitori.tynotes.di.appModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.startKoin() {
    install(Koin) {
        modules(appModule)
    }
}