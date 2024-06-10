package com.tockestoque.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.tockestoque.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtService(
    private val application: Application,
    private val userRepository: UserRepository
) {

    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")

    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    fun createAccessToken(email: String): String =
        createJwtToken(email, 1_800_00)

    fun createRefreshToken(email: String): String =
        createJwtToken(email, 86_400_000)

    private fun createJwtToken(email: String, expireIn: Int): String =
        JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
            .sign(Algorithm.HMAC256(secret))

    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val email = extractEmail(credential)
        val foundUser = email?.let { userEmail ->
            userRepository.findByEmail(userEmail)
        }

        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    fun audienceMatches(audience: String): Boolean =
        this.audience == audience

    private fun audienceMatches(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

    private fun extractEmail(credential: JWTCredential): String? =
        credential.payload.getClaim("email").asString()

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()

    fun extractPrincipalEmail(call: ApplicationCall): String? =
        call.principal<JWTPrincipal>()
            ?.payload
            ?.getClaim("email")
            ?.asString()
}