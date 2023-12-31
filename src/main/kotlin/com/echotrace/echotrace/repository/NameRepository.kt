package com.echotrace.echotrace.repository

import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

data class Name(
    val id: Long?,
    val name: String,
    val userId: Long
)

@Repository
@Profile("!test")
class NameRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : NameRepositoryInterface {

    override fun insert(name: Name) {
        val sql = """
            INSERT INTO echotraceschema.name (name, user_id)
            VALUES (:name, :user_id)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name" to name.name,
                "user_id" to name.userId
            )
        )
    }

    override fun getNames(user: User): List<Name> {
        val sql = """
            SELECT * FROM echotraceschema.name
            WHERE user_id = :user_id
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "user_id" to user.id
            )
        ) { rs, _ ->
            Name(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                userId = rs.getLong("user_id")
            )
        }
    }

    override fun getByName(name: String, user: User): Name? {
        val sql = """
            SELECT * FROM echotraceschema.name
            WHERE name = :name
            AND user_id = :user_id
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "name" to name,
                "user_id" to user.id
            )
        ) { rs, _ ->
            Name(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                userId = rs.getLong("user_id")
            )
        }.firstOrNull()
    }

    override fun getById(id: Long, user: User): Name? {
        val sql = """
            SELECT * FROM echotraceschema.name
            WHERE id = :id
            AND user_id = :user_id
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "id" to id,
                "user_id" to user.id
            )
        ) { rs, _ ->
            Name(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                userId = rs.getLong("user_id")
            )
        }.firstOrNull()
    }

    override fun update(nameUpdated: Name) {
        val sql = """
            UPDATE echotraceschema.name
            SET name = :name
            WHERE id = :id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "id" to nameUpdated.id,
                "name" to nameUpdated.name,
                "user_id" to nameUpdated.userId
            )
        )
    }

    override fun delete(id: Long) {
        val sql = """
            DELETE FROM echotraceschema.name
            WHERE id = :id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "id" to id
            )
        )
    }
}