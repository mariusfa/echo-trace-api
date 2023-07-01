package com.echotrace.echotrace

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.Name
import com.echotrace.echotrace.repository.fakes.EventRepositoryFake
import com.echotrace.echotrace.repository.fakes.NameRepositoryFake
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.OffsetDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventIT(
    @Autowired
    private val mvc: MockMvc,
    @Autowired
    private val nameRepositoryFake: NameRepositoryFake,
    @Autowired
    private val eventRepositoryFake: EventRepositoryFake
) {

    @BeforeEach
    fun setup() {
        nameRepositoryFake.clear()
        eventRepositoryFake.clear()
    }

    @Test
    fun `should post event`() {
        mvc.post("/event") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "name": "test event"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }

        val nameStored = nameRepositoryFake.names[0]
        assert(nameStored?.name == "test event")
        assert(nameStored?.id != null)
        assert(nameRepositoryFake.names.size == 1)

        val eventStored = eventRepositoryFake.events[0]
        assert(eventStored?.name?.id == nameStored?.id)
        assert(eventStored?.createdAt != null)
        assert(eventStored?.id != null)
        assert(eventRepositoryFake.events.size == 1)
    }

    @Test
    fun `should get summaries`() {
        nameRepositoryFake.insert(Name(
            id = null,
            name = "test event"
        ))
        val storedName = nameRepositoryFake.names[0]!!
        eventRepositoryFake.insert(Event(
            id = null,
            name = storedName,
            createdAt = OffsetDateTime.now()
        ))

        mvc.get("/event") {
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                json(
                    """
                        [
                            {
                                "name": "test event",
                                "count": 1
                            }
                        ]
                    """.trimIndent()
                )
            }
        }
    }

    @Test
    fun `should get empty array summary when no events`() {
        mvc.get("/event") {
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                json(
                    """
                        []
                    """.trimIndent()
                )
            }
        }
    }
}