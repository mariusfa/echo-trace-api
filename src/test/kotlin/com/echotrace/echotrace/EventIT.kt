package com.echotrace.echotrace

import com.echotrace.echotrace.repository.fakes.EventRepositoryFake
import com.echotrace.echotrace.repository.fakes.NameRepositoryFake
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

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

    @Test
    fun `should insert event`() {
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
}