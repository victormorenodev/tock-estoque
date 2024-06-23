package com.tockestoque.routing

import com.tockestoque.service.JwtService
import com.tockestoque.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    jwtService: JwtService
) {
    routing {

        route("/api/auth") {
            authRoute(userService)
        }

        route("/api/user") {
            userRoute(userService, jwtService)
        }
    }
}