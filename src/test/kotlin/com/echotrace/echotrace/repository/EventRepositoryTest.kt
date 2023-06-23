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
import java.math.BigInteger
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset


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
        flyway.migrate()
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
            eventName = eventNameStored,
            createdAt = createdAt,
        )

        eventRepository.insertEvent(event)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.events")
        assert(eventsRaw.size == 1)
        assert(eventsRaw[0]["eventname_id"] == eventNameStored.id?.toInt())
        assert(eventsRaw[0]["created_at"] != null)
        val createdAtRaw: Timestamp = eventsRaw[0]["created_at"] as Timestamp
        val createdAtOffset = OffsetDateTime.ofInstant(createdAtRaw.toInstant(), ZoneOffset.ofHours(2))
        assert(createdAtOffset.hour == createdAt.hour)
        assert(createdAtOffset.minute == createdAt.minute)
        assert(createdAtOffset.second == createdAt.second)
        assert(createdAtOffset.dayOfMonth == createdAt.dayOfMonth)
        assert(createdAtOffset.month == createdAt.month)
        assert(createdAtOffset.year == createdAt.year)
        assert(createdAtOffset.offset == createdAt.offset)
    }

    @Test
    fun `test count events for given eventName`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventNameStored = eventNameRepository.getEventNameByName("test event")!!

        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        val count = eventRepository.countEvents(eventNameStored.id!!)
        assert(count == BigInteger.valueOf(2))
    }

    @Test
    fun `test count events when not correct eventName`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        val wrongEventName = EventName(
            id = null,
            name = "wrong event"
        )
        eventNameRepository.insertEventName(eventName)
        eventNameRepository.insertEventName(wrongEventName)
        val eventNameStored = eventNameRepository.getEventNameByName("test event")!!
        val wrongEeventNameStored = eventNameRepository.getEventNameByName("wrong event")!!

        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        val count = eventRepository.countEvents(wrongEeventNameStored.id!!)
        assert(count == BigInteger.valueOf(0))
    }

    @Test
    fun `delete all events given eventNameId`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventNameStored = eventNameRepository.getEventNameByName("test event")!!

        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insertEvent(Event(
            id = null,
            eventName = eventNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.deleteAllEvents(eventNameStored.id!!)
        val count = eventRepository.countEvents(eventNameStored.id!!)
        assert(count == BigInteger.valueOf(0))
    }
}