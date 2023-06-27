package com.echotrace.echotrace.service

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
        val eventRequest = EventRequest(
            name = "test event"
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
        val eventRequest = EventRequest(
            name = "test event"
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
        val eventRequest = EventRequest(
            name = "test event"
        )

        eventService.insert(eventRequest)
        eventService.insert(eventRequest)
        eventService.insert(EventRequest("another event"))

        val summaries = eventService.getAllSummaries()
        assert(summaries.size == 2)
        assert(summaries[0].name == "test event")
        assert(summaries[0].count == BigInteger("2"))
    }

    @Test
    fun `test get all summaries with no events`() {
        val summaries = eventService.getAllSummaries()
        assert(summaries.isEmpty())
    }
}
