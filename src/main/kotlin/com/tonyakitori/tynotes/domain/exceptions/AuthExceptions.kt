package com.tonyakitori.tynotes.domain.exceptions

class BadCredentials(message: String = "Credentials are wrong check your username and password"): Exception(message)