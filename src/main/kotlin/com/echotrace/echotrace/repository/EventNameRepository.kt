package com.echotrace.echotrace.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

data class EventName(
    val id: Long?,
    val name: String,
)

@Repository
class EventNameRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insertEventName(eventName: EventName) {
        val sql = """
            INSERT INTO echotraceschema.eventnames (name)
            VALUES (:name)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name" to eventName.name
            )
        )
    }

    fun getEventNames(): List<EventName> {
        val sql = """
            SELECT * FROM echotraceschema.eventnames
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql
        ) { rs, _ ->
            EventName(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }
    }

    fun getEventNameByName(name: String): EventName? {
        val sql = """
            SELECT * FROM echotraceschema.eventnames
            WHERE name = :name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "name" to name
            )
        ) { rs, _ ->
            EventName(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }.firstOrNull()
    }

    fun updateEventName(eventNameUpdated: EventName) {
        val sql = """
            UPDATE echotraceschema.eventnames
            SET name = :name
            WHERE id = :id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "id" to eventNameUpdated.id,
                "name" to eventNameUpdated.name
            )
        )
    }

    fun deleteEventName(id: Long) {
        val sql = """
            DELETE FROM echotraceschema.eventnames
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