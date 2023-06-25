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
@Import(NameRepository::class, EventRepository::class)
class EventRepositoryTest(
    @Autowired
    private val nameRepository: NameRepository,
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
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameStored  = nameRepository.getByName("test event")!!
        val createdAt = OffsetDateTime.now()
        val event = Event(
            id = null,
            name = nameStored,
            createdAt = createdAt,
        )

        eventRepository.insert(event)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.event")
        assert(eventsRaw.size == 1)
        assert(eventsRaw[0]["name_id"] == nameStored.id?.toInt())
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
    fun `test count events for given name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameStored = nameRepository.getByName("test event")!!

        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        val count = eventRepository.count(nameStored.id!!)
        assert(count == BigInteger.valueOf(2))
    }

    @Test
    fun `test count events when not correct name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        val wrongName = Name(
            id = null,
            name = "wrong event"
        )
        nameRepository.insert(name)
        nameRepository.insert(wrongName)
        val nameStored = nameRepository.getByName("test event")!!
        val wrongNameStored = nameRepository.getByName("wrong event")!!

        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        val count = eventRepository.count(wrongNameStored.id!!)
        assert(count == BigInteger.valueOf(0))
    }

    @Test
    fun `delete all events given name id`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameStored = nameRepository.getByName("test event")!!

        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.deleteAllByNameId(nameStored.id!!)
        val count = eventRepository.count(nameStored.id!!)
        assert(count == BigInteger.valueOf(0))
    }

    @Test
    fun `delete all events given name id and ignore other events`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        val wrongName = Name(
            id = null,
            name = "wrong event"
        )
        nameRepository.insert(name)
        nameRepository.insert(wrongName)
        val nameStored = nameRepository.getByName("test event")!!
        val wrongNameStored = nameRepository.getByName("wrong event")!!

        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insert(Event(
            id = null,
            name = nameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.insert(Event(
            id = null,
            name = wrongNameStored,
            createdAt = OffsetDateTime.now(),
        ))
        eventRepository.deleteAllByNameId(nameStored.id!!)
        val count = eventRepository.count(nameStored.id!!)
        assert(count == BigInteger.valueOf(0))
        val countWrong = eventRepository.count(wrongNameStored.id!!)
        assert(countWrong == BigInteger.valueOf(1))
    }
}