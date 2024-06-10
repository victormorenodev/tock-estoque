package com.tockestoque.service

import com.auth0.jwt.interfaces.DecodedJWT
import com.tockestoque.model.User
import com.tockestoque.repository.RefreshTokenRepository
import com.tockestoque.repository.UserRepository
import com.tockestoque.routing.request.SignInRequest
import com.tockestoque.routing.request.SignUpRequest
import com.tockestoque.routing.response.AuthResponse

class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    suspend fun findAll(): List<User?> =
        userRepository.findAll()

    suspend fun findById(id: Int): User? =
        userRepository.findById(id)

    suspend fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    suspend fun register(params: SignUpRequest): User? {
        val user = userRepository.createUser(params)
        return user
    }

    suspend fun authenticate(signInRequest: SignInRequest): AuthResponse? {
        val email = signInRequest.email
        val foundUser = userRepository.findByEmail(email)

        return if (foundUser != null && foundUser.password == signInRequest.password) {
            val accessToken = jwtService.createAccessToken(email)
            val refreshToken = jwtService.createRefreshToken(email)

            refreshTokenRepository.save(refreshToken, email)
            AuthResponse(accessToken, refreshToken)
        } else null
    }

    suspend fun refreshToken(token: String): String? {
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