package com.tockestoque.repository

class RefreshTokenRepository {

    private val tokens = mutableMapOf<String, String>()

    fun findEmailByToken(token: String): String? =
        tokens[token]

    fun save(token: String, email: String) {
        tokens[token] = email
    }
}