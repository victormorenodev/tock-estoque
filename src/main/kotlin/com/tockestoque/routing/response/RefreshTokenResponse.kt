package com.tockestoque.routing.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val token: String
)
