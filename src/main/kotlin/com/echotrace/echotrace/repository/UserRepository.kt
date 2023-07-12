package com.echotrace.echotrace.repository

import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

data class User(
    val id: Long?,
    val name: String,
    val hashedPassword: String,
    val apiToken: String
)

@Repository
@Profile("!test")
class UserRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : UserRepositoryInterface {

    override fun insert(user: User) {
        val sql = """
            INSERT INTO echotraceschema.user (name, hashed_password, api_token)
            VALUES (:name, :hashed_password, :api_token)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name" to user.name,
                "hashed_password" to user.hashedPassword,
                "api_token" to user.apiToken
            )
        )
    }

    override fun getByName(name: String): User? {
        val sql = """
            SELECT * FROM echotraceschema.user
            WHERE name = :name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "name" to name
            )
        ) { rs, _ ->
            User(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                hashedPassword = rs.getString("hashed_password"),
                apiToken = rs.getString("api_token")
            )
        }.firstOrNull()
    }
}
