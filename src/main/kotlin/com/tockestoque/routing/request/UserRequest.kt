package com.tockestoque.routing.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val createdAt: String,
)
