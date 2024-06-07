package com.tockestoque.service

import com.auth0.jwt.interfaces.DecodedJWT
import com.tockestoque.model.User
import com.tockestoque.repository.RefreshTokenRepository
import com.tockestoque.repository.UserRepository
import com.tockestoque.routing.request.LoginRequest
import com.tockestoque.routing.response.AuthResponse
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun findAll(): List<User> =
        userRepository.findAll()

    fun findById(id: String): User? =
        userRepository.findById(
            id = UUID.fromString(id)
        )

    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    fun save(user: User): User? {
        val foundUser = userRepository.findByUsername(user.username)

        return if (foundUser == null) {
            userRepository.save(user)
            user
        } else null
    }

    fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        val username = loginRequest.username
        val foundUser = userRepository.findByUsername(username)

        return if (foundUser != null && foundUser.password == loginRequest.password) {
            val accessToken = jwtService.createAccessToken(username)
            val refreshToken = jwtService.createRefreshToken(username)

            refreshTokenRepository.save(refreshToken, username)
            AuthResponse(accessToken, refreshToken)
        } else null
    }

    fun refreshToken(token: String): String? {
        val decodedRefreshToken = verifyRefreshToken(token)
        val persistedUsername = refreshTokenRepository.findUsernameByToken(token)

        return if (decodedRefreshToken != null && persistedUsername != null) {
            val foundUser = userRepository.findByUsername(persistedUsername)
            val usernameFromRefreshToken = decodedRefreshToken.getClaim("username").asString()

            if (foundUser != null && usernameFromRefreshToken == foundUser.username)
                jwtService.createAccessToken(persistedUsername)
            else
                null
        } else null
    }

    private fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJWT = decodedJWT(token)

        return decodedJWT?.let {
            val audienceMatches = jwtService.audienceMatches(it.audience.first())

            if (audienceMatches)
                decodedJWT
            else
                null
        }
    }

    private fun decodedJWT(token: String) = try {
        jwtService.jwtVerifier.verify(token)
    } catch (ex: Exception) {
        null
    }
}