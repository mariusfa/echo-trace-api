package com.echotrace.echotrace.controller

import com.echotrace.echotrace.service.UserService
import com.echotrace.echotrace.service.domain.UserRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class UserRequestDTO(
    val username: String,
    val password: String
) {
    fun toDomain(): UserRequest = UserRequest(username, password)
}

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody userRequestDTO: UserRequestDTO) = userService.register(userRequestDTO.toDomain())
}