package com.echotrace.echotrace.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

data class Name(
    val id: Long?,
    val name: String,
)

@Repository
class NameRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insert(name: Name) {
        val sql = """
            INSERT INTO echotraceschema.name (name)
            VALUES (:name)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name" to name.name
            )
        )
    }

    fun getNames(): List<Name> {
        val sql = """
            SELECT * FROM echotraceschema.name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql
        ) { rs, _ ->
            Name(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }
    }

    fun getByName(name: String): Name? {
        val sql = """
            SELECT * FROM echotraceschema.name
            WHERE name = :name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "name" to name
            )
        ) { rs, _ ->
            Name(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }.firstOrNull()
    }

    fun update(nameUpdated: Name) {
        val sql = """
            UPDATE echotraceschema.name
            SET name = :name
            WHERE id = :id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "id" to nameUpdated.id,
                "name" to nameUpdated.name
            )
        )
    }

    fun delete(id: Long) {
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