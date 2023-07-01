package com.echotrace.echotrace.repository

import com.echotrace.echotrace.EchotraceApplication
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [EchotraceApplication::class])
@Import(UserRepository::class)
class UserRepositoryTest(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val flyway: Flyway,
    @Autowired
    private val jdbcTemplate: JdbcTemplate
) {

    @Test
    fun `test insert user`() {
        flyway.migrate()
        val user = User(
            id = null,
            name = "test user",
            hashedPassword = "test hash password",
            apiToken = "test api token"
        )
        userRepository.insert(user)
        val namesRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.user")
        assert(namesRaw.size == 1)
    }

    @Test
    fun `test get user by name`() {
        flyway.migrate()
        val user = User(
            id = null,
            name = "test user",
            hashedPassword = "test hash password",
            apiToken = "test api token"
        )
        userRepository.insert(user)
        val userFound = userRepository.getByName(user.name)
        assert(userFound != null)
        assert(userFound!!.name == user.name)
        assert(userFound.id != null)
        assert(userFound.hashedPassword == user.hashedPassword)
        assert(userFound.apiToken == user.apiToken)
    }
}