package com.tockestoque.repository

import com.tockestoque.security.JwtConfig
import com.tockestoque.security.hash
import com.tockestoque.service.CreateUserParams
import com.tockestoque.service.LoginUserParams
import com.tockestoque.service.UserService
import com.tockestoque.utils.BaseResponse
import io.ktor.http.*

class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): BaseResponse<Any> {
        return if(isEmailExist(params.email)) {
            BaseResponse.ErrorResponse(statusCode = HttpStatusCode.Forbidden, message = "Email already registered")
        } else {
            val user = userService.registerUser(params)
            if (user != null) {
                val token = JwtConfig.instance.createAccessToken(user.id)
                user.authToken = token
                BaseResponse.SuccessResponse(data = user, statusCode = HttpStatusCode.Created)
            } else {
                BaseResponse.ErrorResponse(statusCode = HttpStatusCode.BadRequest)
            }
        }
    }

    override suspend fun loginUser(params: LoginUserParams): BaseResponse<Any> {
        return if(isEmailExist(params.email)) {
            val user = userService.loginUser(params)
            if (user != null) {
                if (isPasswordValid(params.password, user.password)) {
                    val token = JwtConfig.instance.createAccessToken(user.id)
                    user.authToken = token
                    BaseResponse.SuccessResponse(data = user, statusCode = HttpStatusCode.OK)
                } else {
                    BaseResponse.ErrorResponse(message = "Email or password does not match", statusCode = HttpStatusCode.Unauthorized)
                }
            } else {
                BaseResponse.ErrorResponse(statusCode = HttpStatusCode.BadRequest)
            }
        } else {
            BaseResponse.ErrorResponse(message = "Email does not exists", statusCode = HttpStatusCode.Unauthorized)
        }
    }

    private suspend fun isEmailExist(email: String): Boolean {
        return userService.findUserByEmail(email) != null
    }

    private fun isPasswordValid(password: String, hashedPassword: String): Boolean {
        return hash(password) == hashedPassword
    }

}