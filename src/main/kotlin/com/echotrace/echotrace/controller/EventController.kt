package com.echotrace.echotrace.controller

import com.echotrace.echotrace.service.EventRequest
import com.echotrace.echotrace.service.EventService
import org.springframework.web.bind.annotation.*
import java.math.BigInteger

data class EventRequestDTO(
    val name: String,
) {
    fun toDomain(): EventRequest = EventRequest(name)
}

data class SummaryDTO(
    val id: Long,
    val name: String,
    val count: BigInteger
)

@RestController
@RequestMapping("/event")
class EventController(
    private val eventService: EventService
) {

    @PostMapping()
    fun postEvent(@RequestBody eventRequestDTO: EventRequestDTO) = eventService.insert(eventRequestDTO.toDomain())

    @GetMapping()
    fun getEvents(): List<SummaryDTO> = eventService.getAllSummaries().map { it.toDTO() }

}