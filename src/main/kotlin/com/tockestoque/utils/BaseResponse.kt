package com.tockestoque.utils

import io.ktor.http.*

sealed class BaseResponse<T>(
    open val statusCode: HttpStatusCode
) {
    data class SuccessResponse<T>(
        val data: T? = null,
        val message: String? = null,
        override val statusCode: HttpStatusCode
    ): BaseResponse<T>(statusCode = statusCode)

    data class ErrorResponse<T>(
        val exception: T? = null,
        val message: String? = null,
        override val statusCode: HttpStatusCode
    ): BaseResponse<T>(statusCode = statusCode)
}