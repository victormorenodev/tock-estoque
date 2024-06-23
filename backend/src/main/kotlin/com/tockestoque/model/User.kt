package com.tockestoque.model

import java.util.*

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val password: String,
    val createdAt: String
)
