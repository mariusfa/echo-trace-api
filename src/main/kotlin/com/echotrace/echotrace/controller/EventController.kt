package com.echotrace.echotrace.controller

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.service.EventRequest
import com.echotrace.echotrace.service.EventService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.math.BigInteger

data class EventRequestDTO(
    val name: String,
) {
    fun toDomain(user: User): EventRequest = EventRequest(name, user)
}

data class SummaryDTO(
    val id: Long,
    val name: String,
    val count: BigInteger
)

data class EventDetailsDTO(
    val id: Long,
    val name: String,
    val dayCount: List<BigInteger>
)

@RestController
@RequestMapping("/event")
class EventController(
    private val eventService: EventService
) {

    @PostMapping()
    fun postEvent(@RequestBody eventRequestDTO: EventRequestDTO) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        eventService.insert(eventRequestDTO.toDomain(user))
    }

    @GetMapping()
    fun getEvents(): List<SummaryDTO> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return eventService.getAllSummaries(user).map { it.toDTO() }
    }

    @GetMapping("/{id}")
    fun getEventDetails(@PathVariable id: Long): EventDetailsDTO {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return eventService.getDetails(id, user).toDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Long) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        eventService.delete(id, user)
    }
}
