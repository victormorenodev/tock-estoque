package com.tockestoque

import com.tockestoque.plugins.configureSecurity
import com.tockestoque.plugins.configureSerialization
import com.tockestoque.repository.RefreshTokenRepository
import com.tockestoque.repository.UserRepository
import com.tockestoque.routing.configureRouting
import com.tockestoque.service.JwtService
import com.tockestoque.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val jwtService = JwtService(this, userRepository)
    val refreshTokenRepository = RefreshTokenRepository()
    val userService = UserService(userRepository, jwtService, refreshTokenRepository)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(userService)
}
