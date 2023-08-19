package com.echotrace.echotrace.service

import com.echotrace.echotrace.controller.EventDetailsDTO
import com.echotrace.echotrace.repository.*
import com.echotrace.echotrace.service.domain.Summary
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigInteger
import java.time.OffsetDateTime


data class EventRequest(
    val name: String,
    val user: User
)

data class EventDetails(
    val id: Long,
    val name: String,
    val dayCount: List<BigInteger>
) {
    fun toDTO(): EventDetailsDTO = EventDetailsDTO(id, name, dayCount)
}

@Service
class EventService(
    private val eventRepository: EventRepositoryInterface,
    private val nameRepository: NameRepositoryInterface
) {
    fun insert(eventRequest: EventRequest) {
        if (nameRepository.getByName(eventRequest.name, eventRequest.user) == null) {
            nameRepository.insert(Name(null, eventRequest.name, eventRequest.user.id!!))
        }

        val name = nameRepository.getByName(eventRequest.name, eventRequest.user)!!
        eventRepository.insert(Event(null, name, OffsetDateTime.now()))

    }

    fun getAllSummaries(user: User): List<Summary> {
        val names = nameRepository.getNames(user)
        return names.map { Summary(id = it.id!!, name = it.name, count = eventRepository.count(it.id)) }
    }

    fun getDetails(nameId: Long, user: User): EventDetails {
        val name = nameRepository.getById(nameId, user)
        if (name == null || name.userId != user.id) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Name not found")
        }

        val dayCount = eventRepository.getCountEachDay(nameId, 30)
        return EventDetails(nameId, name.name, dayCount)
    }
}