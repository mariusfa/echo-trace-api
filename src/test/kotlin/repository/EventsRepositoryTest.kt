package repository

import com.echotrace.echotrace.EchotraceApplication
import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventsRepository
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [EchotraceApplication::class])
@Import(EventsRepository::class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventsRepositoryTest(
    @Autowired
    private val eventsRepository: EventsRepository,
    @Autowired
    private val flyway: Flyway,
    @Autowired
    private val jdbcTemplate: JdbcTemplate
) {

    @BeforeAll
    fun setup() {
        flyway.migrate()
    }

    @Test
    fun `test config`() {
    }

    @Test
    fun `test insert event`() {
        val event = Event(
            id = null,
            name = "test event"
        )
        eventsRepository.insertEvent(event)
        val eventsRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.events")
        assert(eventsRaw.size == 1)
    }

//    @Test
//    fun `test get events`() {
//        val event = Event(
//            id = null,
//            name = "test event"
//        )
//        eventsRepository.insertEvent(event)
//        val events = eventsRepository.getEvents(1)
//
//    }
}