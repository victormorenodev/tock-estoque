package com.tockestoque

import com.tockestoque.db.DatabaseFactory
import com.tockestoque.plugins.configureSecurity
import com.tockestoque.plugins.configureSerialization
import com.tockestoque.repository.RefreshTokenRepository
import com.tockestoque.repository.UserRepository
import com.tockestoque.routing.configureRouting
import com.tockestoque.service.JwtService
import com.tockestoque.service.SecurityService
import com.tockestoque.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    val userRepository = UserRepository()
    val jwtService = JwtService(this, userRepository)
    val refreshTokenRepository = RefreshTokenRepository()
    val securityService = SecurityService()
    val userService = UserService(userRepository, jwtService, refreshTokenRepository, securityService)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(userService, jwtService)
}