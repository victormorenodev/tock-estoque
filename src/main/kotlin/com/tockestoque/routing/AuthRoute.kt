package com.tockestoque.routing

import com.tockestoque.routing.request.SignInRequest
import com.tockestoque.routing.request.RefreshTokenRequest
import com.tockestoque.routing.request.SignUpRequest
import com.tockestoque.routing.response.AuthResponse
import com.tockestoque.routing.response.RefreshTokenResponse
import com.tockestoque.routing.response.UserResponse
import com.tockestoque.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(userService: UserService) {
    // Refresh token route
    post("/refresh") {
        val request = call.receive<RefreshTokenRequest>()
        val newAccessToken: String? = userService.refreshToken(request.token)
        newAccessToken?.let {
            call.respond(
                RefreshTokenResponse(it)
            )
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized
        )
    }
}