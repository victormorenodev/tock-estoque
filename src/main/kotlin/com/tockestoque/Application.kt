package com.tockestoque

import com.tockestoque.plugins.configureSecurity
import com.tockestoque.plugins.configureSerialization
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
    val userService = UserService(userRepository)
    val jwtService = JwtService(this, userService)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(userService, jwtService)
}
