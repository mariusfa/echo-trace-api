package repository

import com.echotrace.echotrace.EchotraceApplication
import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventsRepository
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
@Import(EventsRepository::class)
class EventsRepositoryTest(
    @Autowired
    private val eventsRepository: EventsRepository,
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
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.events")
        assert(eventsRaw.size == 1)
    }

    @Test
    fun `test get events`() {
        flyway.migrate()
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val events = eventsRepository.getEvents()
        assert(events.size == 1)
        assert(events[0].name == "test event")
    }

    @Test
    fun `test get event by name`() {
        flyway.migrate()
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val eventFound = eventsRepository.getEventByName(event.name)
        assert(eventFound != null)
        assert((eventFound?.name ?: "") == event.name)
    }

    @Test
    fun `test update event`() {
        flyway.migrate()
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val eventFound = eventsRepository.getEventByName(event.name)

        val eventUpdated = Event(
            id = eventFound?.id,
            name = "test event updated"
        )
        eventsRepository.updateEvent(eventUpdated)
        val eventFoundUpdated = eventsRepository.getEventByName(eventUpdated.name)
        assert(eventFoundUpdated != null)
        assert((eventFoundUpdated?.name ?: "") == eventUpdated.name)
    }

    @Test
    fun `test delete event`() {
        flyway.migrate()
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val eventFound = eventsRepository.getEventByName(event.name)
        assert(eventFound != null)
        eventsRepository.deleteEvent(eventFound?.id ?: 0)
        val eventFoundDeleted = eventsRepository.getEventByName(event.name)
        assert(eventFoundDeleted == null)
    }
}