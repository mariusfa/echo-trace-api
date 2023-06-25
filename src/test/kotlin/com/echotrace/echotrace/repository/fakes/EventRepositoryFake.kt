package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventRepositoryInterface
import java.math.BigInteger

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
}