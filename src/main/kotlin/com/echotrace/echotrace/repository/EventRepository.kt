package com.echotrace.echotrace.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.OffsetDateTime

data class Event(
    val id: Long?,
    val eventName: EventName,
    val createdAt: OffsetDateTime,
)

@Repository
class EventRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insertEvent(event: Event) {
        val sql = """
            INSERT INTO echotraceschema.events (eventname_id, created_at)
            VALUES (:eventname_id, :created_at)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "eventname_id" to event.eventName.id!!,
                "created_at" to event.createdAt
            )
        )

    }

    fun countEvents(eventNameId: Long): BigInteger {
        val sql = """
            SELECT COUNT(*) FROM echotraceschema.events
            WHERE eventname_id = :eventname_id
        """.trimIndent()

        return namedParameterJdbcTemplate.queryForObject(
            sql,
            mapOf(
                "eventname_id" to eventNameId
            ),
            BigInteger::class.java
        ) ?: BigInteger.ZERO
    }


}