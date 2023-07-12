package com.echotrace.echotrace.service

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.repository.fakes.UserRepositoryFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JwtServiceTest {

    private val userRepo = UserRepositoryFake()

    @BeforeEach
    fun setup() {
        userRepo.clear()
        userRepo.insert(User(id = 1, name = "test", hashedPassword = "hashedPassword", apiToken = "apiToken"))
    }

    @Test
    fun `test should get jwt token`() {
        val jwtService = JwtService(userRepository = userRepo)
        val token = jwtService.getToken(username = "test")
        assert(token != null)
    }

    @Test
    fun `test should get user from jwt token`() {
        val jwtService = JwtService(userRepository = userRepo)
        val token = jwtService.getToken(username = "test")
        val user: User? = jwtService.getUser(token)
        assert(user != null)
        assert(user!!.name == "test")
    }

    @Test
    fun `test should validate token - wrong secret`() {
        val jwtServiceCorrect = JwtService(userRepository = userRepo)
        val jwtServiceWrong = JwtService(userRepository = userRepo)
        val tokenWrong = jwtServiceWrong.getToken("test")
        assert(jwtServiceCorrect.getUser(tokenWrong) == null)
    }

    @Test
    fun `test should validate token - expired date`() {
        val jwtServcie = JwtService(userRepository = userRepo)
        val token = jwtServcie.getToken(username = "test", expirationTime = -1)
        assert(jwtServcie.getUser(token) == null)
    }

    @Test
    fun `test should validate user - wrong user`() {
        val jwtService = JwtService(userRepository = userRepo)
        val token = jwtService.getToken(username = "wrong")
        assert(jwtService.getUser(token) == null)
    }
}