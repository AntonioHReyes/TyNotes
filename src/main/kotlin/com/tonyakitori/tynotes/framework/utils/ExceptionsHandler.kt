package com.tonyakitori.tynotes.framework.utils

import com.google.gson.stream.MalformedJsonException
import com.tonyakitori.tynotes.domain.exceptions.PagePropertyNotFound
import com.tonyakitori.tynotes.domain.exceptions.ProfileNotFound
import com.tonyakitori.tynotes.domain.exceptions.SizePropertyNotFound
import com.tonyakitori.tynotes.domain.exceptions.UserFoundButDisabled
import com.tonyakitori.tynotes.domain.exceptions.UserIdNotGenerated
import com.tonyakitori.tynotes.domain.exceptions.UserIdNotValid
import com.tonyakitori.tynotes.domain.exceptions.UserNameOrEmailExist
import com.tonyakitori.tynotes.domain.exceptions.UserNotFound
import com.tonyakitori.tynotes.domain.exceptions.callSimpleMessage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*


suspend fun PipelineContext<*, ApplicationCall>.handleGeneralExceptions(e: Exception){
    when (e) {
        is MalformedJsonException -> this.call.respond(HttpStatusCode.BadRequest, callSimpleMessage("Json invalid"))
        is NullPointerException -> call.respond(HttpStatusCode.BadRequest, callSimpleMessage(e.toString()))
        else -> call.respond(HttpStatusCode.InternalServerError, callSimpleMessage(e.toString()))
    }
}

suspend fun PipelineContext<*, ApplicationCall>.handlePaginationExceptions(e: Exception){
    when(e){
        is PagePropertyNotFound -> this.call.respond(HttpStatusCode.BadRequest, callSimpleMessage(e.message))
        is SizePropertyNotFound -> this.call.respond(HttpStatusCode.BadRequest, callSimpleMessage(e.message))
        e -> handleGeneralExceptions(e)
    }
}

suspend fun PipelineContext<*, ApplicationCall>.handleUserExceptions(e: Exception){
    when(e){
        is UserIdNotValid -> this.call.respond(HttpStatusCode.BadRequest, callSimpleMessage(e.message))
        is UserNotFound -> this.call.respond(HttpStatusCode.NotFound, callSimpleMessage(e.message))
        is UserNameOrEmailExist -> this.call.respond(HttpStatusCode.BadRequest, callSimpleMessage(e.message))
        is UserFoundButDisabled -> this.call.respond(HttpStatusCode.Conflict, mapOf("message" to e.message))
        is UserIdNotGenerated -> this.call.respond(HttpStatusCode.InternalServerError, callSimpleMessage(e.message))
        is ProfileNotFound -> this.call.respond(HttpStatusCode.InternalServerError, callSimpleMessage(e.message))
        else -> handlePaginationExceptions(e)
    }
}