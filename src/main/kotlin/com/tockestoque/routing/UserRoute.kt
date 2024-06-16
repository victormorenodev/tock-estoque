package com.tockestoque.routing

import com.tockestoque.model.User
import com.tockestoque.routing.request.SignInRequest
import com.tockestoque.routing.request.SignUpRequest
import com.tockestoque.routing.response.AuthResponse
import com.tockestoque.routing.response.UserResponse
import com.tockestoque.service.JwtService
import com.tockestoque.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userService: UserService, jwtService: JwtService) {

    authenticate {
        get("/{id}") {
            val id  = (call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest))

            val foundUser = userService.findById(id.toInt())
                ?: return@get call.respond(HttpStatusCode.NotFound)

            if (foundUser.email != jwtService.extractPrincipalEmail(call))
                return@get call.respond(HttpStatusCode.NotFound)

                call.respond(
                    message = foundUser.toResponse()
                )
        }
    }

    post("/signup") {
        val params = call.receive<SignUpRequest>()
        if(userService.findByEmail(params.email) != null) {
            call.respond(HttpStatusCode.BadRequest, message = "User already exists")
        } else {
            val user = userService.register(params)
            if (user != null) {
                call.respond(HttpStatusCode.Created, user.toResponse())
            }
            else call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/signin") {
        val params = call.receive<SignInRequest>()
        val user = userService.findByEmail(params.email)
        if(user == null) {
            call.respond(HttpStatusCode.NotFound, message = "User not found")
        } else {
            if(user.password == params.password) {
                val accessToken = jwtService.createAccessToken(user.email)
                val refreshToken = jwtService.createRefreshToken(user.email)
                return@post call.respond(HttpStatusCode.OK, AuthResponse(accessToken = accessToken, refreshToken = refreshToken))
            } else {
                return@post call.respond(HttpStatusCode.Unauthorized, message = "Wrong password")
            }
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