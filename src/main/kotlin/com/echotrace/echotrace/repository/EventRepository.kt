package com.echotrace.echotrace.repository

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.OffsetDateTime

data class Event(
    val id: Long?,
    val name: Name,
    val createdAt: OffsetDateTime,
)

@Repository
class EventRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insert(event: Event) {
        val sql = """
            INSERT INTO echotraceschema.event (name_id, created_at)
            VALUES (:name_id, :created_at)
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name_id" to event.name.id!!,
                "created_at" to event.createdAt
            )
        )

    }

    fun count(nameId: Long): BigInteger {
        val sql = """
            SELECT COUNT(*) FROM echotraceschema.event
            WHERE name_id = :name_id
        """.trimIndent()

        return namedParameterJdbcTemplate.queryForObject(
            sql,
            mapOf(
                "name_id" to nameId
            ),
            BigInteger::class.java
        ) ?: BigInteger.ZERO
    }

    fun deleteAllByNameId(nameId: Long) {
        val sql = """
            DELETE FROM echotraceschema.event
            WHERE name_id = :name_id
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            mapOf(
                "name_id" to nameId
            )
        )
    }

}