package com.bicisos.i7.bicisos.model

import java.io.Serializable

data class UserResponse (
    val user: User,
    val token: String
) : Serializable

data class User (
    val email: String,
    val nombre: String,
    val id: String
)
