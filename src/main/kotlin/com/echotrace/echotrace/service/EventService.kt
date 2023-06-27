package com.echotrace.echotrace.service

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventRepositoryInterface
import com.echotrace.echotrace.repository.Name
import com.echotrace.echotrace.repository.NameRepositoryInterface
import com.echotrace.echotrace.service.domain.Summary
import org.springframework.stereotype.Service
import java.time.OffsetDateTime


data class EventRequest(
    val name: String,
)

@Service
class EventService(
    private val eventRepository: EventRepositoryInterface,
    private val nameRepository: NameRepositoryInterface
) {
    fun insert(eventRequest: EventRequest) {
        if (nameRepository.getByName(eventRequest.name) == null) {
            nameRepository.insert(Name(null, eventRequest.name))
        }

        val name = nameRepository.getByName(eventRequest.name)!!
        eventRepository.insert(Event(null, name, OffsetDateTime.now()))

    }

    fun getAllSummaries(): List<Summary> {
        val names = nameRepository.getNames()
        return names.map { Summary(id = it.id!!, name = it.name, count = eventRepository.count(it.id)) }
    }


}