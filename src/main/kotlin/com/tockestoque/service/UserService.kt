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

    suspend fun findAll(): List<User> =
        userRepository.findAll()

    fun findById(id: Int): User? =
        userRepository.findById(id)

    fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun save(user: User): User? {
        TODO("To do")
    }

    fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        val email = loginRequest.email
        val foundUser = userRepository.findByEmail(email)

        return if (foundUser != null && foundUser.password == loginRequest.password) {
            val accessToken = jwtService.createAccessToken(email)
            val refreshToken = jwtService.createRefreshToken(email)

            refreshTokenRepository.save(refreshToken, email)
            AuthResponse(accessToken, refreshToken)
        } else null
    }

    fun refreshToken(token: String): String? {
        val decodedRefreshToken = verifyRefreshToken(token)
        val persistedEmail = refreshTokenRepository.findEmailByToken(token)

        return if (decodedRefreshToken != null && persistedEmail != null) {
            val foundUser = userRepository.findByEmail(persistedEmail)
            val usernameFromRefreshToken = decodedRefreshToken.getClaim("email").asString()

            if (foundUser != null && usernameFromRefreshToken == foundUser.email)
                jwtService.createAccessToken(persistedEmail)
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