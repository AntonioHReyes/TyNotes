package com.tonyakitori.tynotes.domain.exceptions

fun callSimpleMessage(message: String?): Map<String, String>{
    return mapOf("message" to (message ?: "Unknown Error"))
}