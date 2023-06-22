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

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [EchotraceApplication::class])
@Import(EventNameRepository::class)
class EventNameRepositoryTest(
    @Autowired
    private val eventNameRepository: EventNameRepository,
    @Autowired
    private val flyway: Flyway,
    @Autowired
    private val jdbcTemplate: JdbcTemplate
) {
    @Test
    fun `test config`() {
    }

    @Test
    fun `test insert EventName`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.eventnames")
        assert(eventsRaw.size == 1)
    }

    @Test
    fun `test get EventNames`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val events = eventNameRepository.getEventNames()
        assert(events.size == 1)
        assert(events[0].name == "test event")
    }

    @Test
    fun `test get EventName by name`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventFound = eventNameRepository.getEventNameByName(eventName.name)
        assert(eventFound != null)
        assert((eventFound?.name ?: "") == eventName.name)
    }

    @Test
    fun `test update EventName`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventFound = eventNameRepository.getEventNameByName(eventName.name)

        val eventNameUpdated = EventName(
            id = eventFound?.id,
            name = "test event updated"
        )
        eventNameRepository.updateEventName(eventNameUpdated)
        val eventFoundUpdated = eventNameRepository.getEventNameByName(eventNameUpdated.name)
        assert(eventFoundUpdated != null)
        assert((eventFoundUpdated?.name ?: "") == eventNameUpdated.name)
    }

    @Test
    fun `test delete EventName`() {
        flyway.migrate()
        val eventName = EventName(
            id = null,
            name = "test event"
        )
        eventNameRepository.insertEventName(eventName)
        val eventFound = eventNameRepository.getEventNameByName(eventName.name)
        assert(eventFound != null)
        eventNameRepository.deleteEventName(eventFound?.id ?: 0)
        val eventFoundDeleted = eventNameRepository.getEventNameByName(eventName.name)
        assert(eventFoundDeleted == null)
    }
}