package com.tonyakitori.tynotes.domain.exceptions

class UserIdNotValid(message: String? = "User id not valid"): Exception(message)
class UserNotFound(message: String? = "User not found"): Exception(message)
class UserNameOrEmailExist(message: String? = "User found in db"): Exception(message)
class UserFoundButDisabled(message: String? = "User exist but have a previous elimination"): Exception(message)
class UserIdNotGenerated(message: String? = "Error happens in get userId"): Exception(message)