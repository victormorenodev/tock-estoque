package com.tockestoque

import com.tockestoque.db.DatabaseFactory
import com.tockestoque.repository.UserRepository
import com.tockestoque.repository.UserRepositoryImpl
import com.tockestoque.routes.authRoutes
import com.tockestoque.security.configureSecurity
import com.tockestoque.service.UserService
import com.tockestoque.service.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.jackson.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    install(ContentNegotiation) {
        jackson()
    }
    configureSecurity()
    val service: UserService = UserServiceImpl()
    val repository: UserRepository = UserRepositoryImpl(service)
    authRoutes(repository)

    routing {
        authenticate {
            get("testurl") {
                call.respond("Working fine")
            }
        }
    }
}
