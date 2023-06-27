package com.echotrace.echotrace.repository

import org.springframework.context.annotation.Profile
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
@Profile("!test")
class EventRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : EventRepositoryInterface {

    override fun insert(event: Event) {
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

    override fun count(nameId: Long): BigInteger {
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

    override fun deleteAllByNameId(nameId: Long) {
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