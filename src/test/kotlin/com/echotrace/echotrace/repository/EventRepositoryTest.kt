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
import java.time.OffsetDateTime


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [EchotraceApplication::class])
@Import(EventNameRepository::class, EventRepository::class)
class EventRepositoryTest(
    @Autowired
    private val eventNameRepository: EventNameRepository,
    @Autowired
    private val eventRepository: EventRepository,
    @Autowired
    private val flyway: Flyway,
    @Autowired
    private val jdbcTemplate: JdbcTemplate
) {

    @Test
    fun `test config`() {
    }

    @Test
    fun `test insert event`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventNameStored = eventNameRepository.getEventNameByName("test event")!!
        val createdAt = OffsetDateTime.now()
        val event = Event(
            id = null,
            eventName = eventName,
            createdAt = createdAt,
        )

        eventRepository.insertEvent(event)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.events")
        assert(eventsRaw.size == 1)
        assert(eventsRaw[0]["eventname_id"] == eventNameStored.id)
        assert(eventsRaw[0]["created_at"] == createdAt)
    }
}