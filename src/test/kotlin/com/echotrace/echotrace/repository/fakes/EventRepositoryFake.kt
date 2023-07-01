package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventRepositoryInterface
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import java.math.BigInteger

@Repository
@Primary
class EventRepositoryFake() : EventRepositoryInterface {

    val events = mutableMapOf<Long, Event>()
    var id = 0L

    override fun insert(event: Event) {
        events[id] = Event(id, event.name, event.createdAt)
        id++
    }

    override fun count(nameId: Long): BigInteger {
        val correctEvents = events.filter { it.value.name.id == nameId }
        return BigInteger.valueOf(correctEvents.size.toLong())
    }

    override fun deleteAllByNameId(nameId: Long) {
        events.remove(nameId)
    }

    fun clear() {
        events.clear()
        id = 0L
    }
}