package com.tockestoque.model

import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val password: String
)
