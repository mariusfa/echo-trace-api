package com.echotrace.echotrace.service

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.repository.UserRepositoryInterface
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    private val userRepository: UserRepositoryInterface,
) {
    private val key: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun getToken(username: String, expirationTime: Int = 60 * 60 * 24): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key)
            .compact()
    }

    fun getUser(token: String): User? {
        runCatching {
            val username = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
            return userRepository.getByName(username)
        }.onFailure {
            return null
        }
        return null
    }
}