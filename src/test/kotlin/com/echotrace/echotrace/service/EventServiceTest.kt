package com.echotrace.echotrace.service

import org.junit.jupiter.api.Test

class EventServiceTest {

    private val eventRepository = EventRepositoryFake()
    private val eventNameRepository = EventNameRepositoryFake()
    private val eventService = EventService(eventRepository, eventNameRepository)

    @Test
    fun `test insert first event also create eventName`() {
        val eventRequest = EventRequest(
            name = "test event"
        )

        eventService.insertEvent(eventRequest)

        assert(eventRepository.events.size == 1)
        assert(eventNameRepository.eventNames.size == 1)
        assert(eventNameRepository.eventNames[0].name == "test event")
        assert(eventRepository.events[0].eventName.id == eventNameRepository.eventNames[0].id)
        assert(eventRepository.events[0].createdAt != null)
    }
}