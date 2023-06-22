package com.echotrace.echotrace.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

data class Event(
    val id: Long?,
    val name: String,
)

@Repository
class EventsRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insertEvent(event: Event) {
        val sql = """
            INSERT INTO echotraceschema.events (name)
            VALUES (:name)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name" to event.name
            )
        )
    }

    fun getEvents(): List<Event> {
        val sql = """
            SELECT * FROM echotraceschema.events
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql
        ) { rs, _ ->
            Event(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }
    }

    fun getEventByName(name: String): Event? {
        val sql = """
            SELECT * FROM echotraceschema.events
            WHERE name = :name
        """.trimIndent()

        return namedParameterJdbcTemplate.query(
            sql,
            mapOf(
                "name" to name
            )
        ) { rs, _ ->
            Event(
                id = rs.getLong("id"),
                name = rs.getString("name")
            )
        }.firstOrNull()
    }

    fun updateEvent(eventUpdated: Event) {
        val sql = """
            UPDATE echotraceschema.events
            SET name = :name
            WHERE id = :id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "id" to eventUpdated.id,
                "name" to eventUpdated.name
            )
        )
    }

    fun deleteEvent(id: Long) {
        val sql = """
            DELETE FROM echotraceschema.events
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