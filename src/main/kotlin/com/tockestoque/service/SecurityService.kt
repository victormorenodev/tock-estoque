package com.tockestoque.service

import org.mindrot.jbcrypt.BCrypt

class SecurityService {
    private val SALT_LENGTH = 16

    fun generateSalt(): String {
        return BCrypt.gensalt(SALT_LENGTH)
    }

    fun hashPassword(
        password: String,
        salt: String
    ): String {
        return BCrypt.hashpw(password, salt)
    }

    fun isCorrectPassword(
        plainText: String,
        hash: String
    ): Boolean {
        return BCrypt.checkpw(plainText, hash)
    }
}