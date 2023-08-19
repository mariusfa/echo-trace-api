package com.echotrace.echotrace

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.Name
import com.echotrace.echotrace.repository.fakes.EventRepositoryFake
import com.echotrace.echotrace.repository.fakes.NameRepositoryFake
import com.echotrace.echotrace.repository.fakes.UserRepositoryFake
import com.echotrace.echotrace.service.UserService
import com.echotrace.echotrace.service.domain.UserRequest
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
    private val eventRepositoryFake: EventRepositoryFake,
    @Autowired
    private val userRepositoryFake: UserRepositoryFake,
    @Autowired
    private val userService: UserService

) {

    @BeforeEach
    fun setup() {
        nameRepositoryFake.clear()
        eventRepositoryFake.clear()
        userRepositoryFake.clear()
    }

    @Test
    fun `should post event`() {
        val user = UserRequest("test user", "test password")
        userService.register(user)
        val apiToken = userRepositoryFake.users[0]?.apiToken

        mvc.post("/event") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "name": "test event"
                }
            """.trimIndent()
            header("Authorization", "Api $apiToken")
        }.andExpect {
            status { isOk() }
        }

        val nameStored = nameRepositoryFake.names[0]
        assert(nameStored?.name == "test event")
        assert(nameStored?.id != null)
        assert(nameStored?.userId != null)
        assert(nameRepositoryFake.names.size == 1)

        val eventStored = eventRepositoryFake.events[0]
        assert(eventStored?.name?.id == nameStored?.id)
        assert(eventStored?.createdAt != null)
        assert(eventStored?.id != null)
        assert(eventRepositoryFake.events.size == 1)
    }

    @Test
    fun `should not be able to post event`() {
        val apiToken = "invalid token"
        mvc.post("/event") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "name": "test event"
                }
            """.trimIndent()
            header("Authorization", "Api $apiToken")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `should get summaries`() {
        val user = UserRequest("test user", "test password")
        userService.register(user)
        val token = userService.login(user)
        val userId = userRepositoryFake.users[0]?.id

        nameRepositoryFake.insert(Name(
            id = null,
            name = "test event",
            userId = userId!!
        ))
        val storedName = nameRepositoryFake.names[0]!!
        eventRepositoryFake.insert(Event(
            id = null,
            name = storedName,
            createdAt = OffsetDateTime.now()
        ))

        mvc.get("/event") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
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
        val user = UserRequest("test user", "test password")
        userService.register(user)
        val token = userService.login(user)

        mvc.get("/event") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
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

    @Test
    fun `should not get summaries for other users`() {
        val user = UserRequest("test user", "test password")
        userService.register(user)
        userService.register(user.copy(username = "other user"))
        val otherUser = userRepositoryFake.getByName("other user")!!

        nameRepositoryFake.insert(Name(
            id = null,
            name = "test event",
            userId = otherUser.id!!
        ))
        val storedName = nameRepositoryFake.names[0]!!
        eventRepositoryFake.insert(Event(
            id = null,
            name = storedName,
            createdAt = OffsetDateTime.now()
        ))

        val token = userService.login(user)

        mvc.get("/event") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
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

    @Test
    fun `should get forbidden when invalid token`() {
        val token = "invalid token"
        mvc.get("/event") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isForbidden() }
        }

    }

    @Test
    fun `should get event details with count for last 30 days`() {
        val user = UserRequest("test user", "test password")
        userService.register(user)
        val token = userService.login(user)
        val userId = userRepositoryFake.users[0]?.id

        nameRepositoryFake.insert(Name(
            id = null,
            name = "test event",
            userId = userId!!
        ))
        val storedName = nameRepositoryFake.names[0]!!
        val event = Event(
            id = null,
            name = storedName,
            createdAt = OffsetDateTime.now()
        )
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(1)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(1)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(2)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(2)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(2)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(3)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(3)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(3)))
        eventRepositoryFake.insert(event.copy(createdAt = OffsetDateTime.now().minusDays(3)))

        mvc.get("/event/${storedName.id}") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("test event") }
            jsonPath("$.dayCount[0]") { value(0) }
            jsonPath("$.dayCount[1]") { value(2) }
            jsonPath("$.dayCount[2]") { value(3) }
            jsonPath("$.dayCount[3]") { value(4) }
            jsonPath("$.dayCount[5]") { value(0) }
        }

    }
}