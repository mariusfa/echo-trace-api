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
    // create a function to insert an event into the database
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
}