package com.tockestoque.routing

import com.tockestoque.model.User
import com.tockestoque.routing.request.UserRequest
import com.tockestoque.routing.response.UserResponse
import com.tockestoque.service.JwtService
import com.tockestoque.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoute(userService: UserService, jwtService: JwtService) {

    post("/register") {
       TODO("Todo")
    }

    authenticate {
        get("/login") {
            TODO("Todo")
        }
    }

    authenticate("another-auth") {
        get("/{id}") {
            val id: Int = (call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)) as Int

            val foundUser = userService.findById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            if (foundUser.email != jwtService.extractPrincipalEmail(call))
                return@get call.respond(HttpStatusCode.NotFound)

                call.respond(
                    message = foundUser.toResponse()
                )
        }
    }

}
private fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id,
        email = this.email,
        fullName = this.fullName,
        createdAt = this.createdAt
    )