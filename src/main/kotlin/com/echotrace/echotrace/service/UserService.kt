package com.echotrace.echotrace.service

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.repository.UserRepositoryInterface
import com.echotrace.echotrace.service.domain.UserRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepo: UserRepositoryInterface
) {
    fun register(userRequest: UserRequest) {
        if (userRepo.getByName(userRequest.username) != null) throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists")
        val user = User(
            id = null,
            name = userRequest.username,
            hashedPassword = hashPassword(userRequest.password),
            apiToken = apiToken()
        )
        userRepo.insert(user)
    }

    private fun apiToken(): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..32)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun hashPassword(password: String): String {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.encode(password)
    }
}