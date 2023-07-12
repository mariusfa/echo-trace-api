package com.echotrace.echotrace.controller

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.service.UserService
import com.echotrace.echotrace.service.domain.UserRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

data class UserRequestDTO(
    val username: String,
    val password: String
) {
    fun toDomain(): UserRequest = UserRequest(username, password)
}

data class TokenDTO(
    val token: String
)

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody userRequestDTO: UserRequestDTO) = userService.register(userRequestDTO.toDomain())

    @PostMapping("/login")
    fun login(@RequestBody userRequestDTO: UserRequestDTO) = TokenDTO(userService.login(userRequestDTO.toDomain()))

    @GetMapping("/api-token")
    fun apiToken(): TokenDTO {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return TokenDTO(userService.getApiToken(user.name))
    }
}