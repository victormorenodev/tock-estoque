package com.tockestoque.routing.response

import com.tockestoque.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val fullName: String,
    val createdAt: String
)