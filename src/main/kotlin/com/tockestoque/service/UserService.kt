package com.tockestoque.service

import com.tockestoque.models.User

interface UserService {
    suspend fun registerUser(params: CreateUserParams): User? //nullable
    suspend fun findUserByEmail(email: String): User?
}