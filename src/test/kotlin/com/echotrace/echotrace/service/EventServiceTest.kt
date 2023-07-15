package com.echotrace.echotrace.service

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.repository.fakes.EventRepositoryFake
import com.echotrace.echotrace.repository.fakes.NameRepositoryFake
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EventServiceTest {

    private val eventRepository = EventRepositoryFake()
    private val nameRepository = NameRepositoryFake()
    private val eventService = EventService(eventRepository, nameRepository)

    @Test
    fun `test insert first event also create eventName`() {
        val user = User(
            id = 1,
            name = "test user",
            hashedPassword = "test password",
            apiToken = "test token"
        )
        val eventRequest = EventRequest(
            name = "test event",
            user = user
        )

        eventService.insert(eventRequest)

        val nameStored = nameRepository.names[0]
        assert(nameStored?.name == "test event")
        assert(nameStored?.id != null)
        assert(nameRepository.names.size == 1)

        val eventStored = eventRepository.events[0]
        assert(eventStored?.name?.id == nameStored?.id)
        assert(eventStored?.createdAt != null)
        assert(eventStored?.id != null)
        assert(eventRepository.events.size == 1)
    }

    @Test
    fun `test insert 2 events, only on creates a name`() {
        val user = User(
            id = 1,
            name = "test user",
            hashedPassword = "test password",
            apiToken = "test token"
        )
        val eventRequest = EventRequest(
            name = "test event",
            user = user
        )

        eventService.insert(eventRequest)
        eventService.insert(eventRequest)

        val nameStored = nameRepository.names[0]
        assert(nameStored?.name == "test event")
        assert(nameStored?.id != null)
        assert(nameRepository.names.size == 1)

        val eventStored = eventRepository.events[0]
        assert(eventStored?.name?.id == nameStored?.id)
        assert(eventStored?.createdAt != null)
        assert(eventStored?.id != null)
        assert(eventRepository.events.size == 2)
    }

    @Test
    fun `test get all summaries`() {
        val user = User(
            id = 1,
            name = "test user",
            hashedPassword = "test password",
            apiToken = "test token"
        )
        val eventRequest = EventRequest(
            name = "test event",
            user = user
        )

        eventService.insert(eventRequest)
        eventService.insert(eventRequest)
        eventService.insert(EventRequest("another event", user))

        val summaries = eventService.getAllSummaries(user)
        assert(summaries.size == 2)
        assert(summaries[0].name == "test event")
        assert(summaries[0].count == BigInteger("2"))
    }

    @Test
    fun `test get all summaries with no events`() {
        val user = User(
            id = 1,
            name = "test user",
            hashedPassword = "test password",
            apiToken = "test token"
        )
        val summaries = eventService.getAllSummaries(user)
        assert(summaries.isEmpty())
    }
}
