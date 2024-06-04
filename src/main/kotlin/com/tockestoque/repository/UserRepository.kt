package com.tockestoque.repository

import com.tockestoque.models.User
import com.tockestoque.service.CreateUserParams
import com.tockestoque.utils.BaseResponse

interface UserRepository {
    suspend fun registerUser(params : CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(email: String, password: String): BaseResponse<Any>
}