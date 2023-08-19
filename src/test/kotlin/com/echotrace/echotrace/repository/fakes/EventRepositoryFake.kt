package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.Event
import com.echotrace.echotrace.repository.EventRepositoryInterface
import com.echotrace.echotrace.repository.Name
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDate
import java.time.OffsetDateTime


data class Event(
    val id: Long?,
    val name: Name,
    val createdAt: OffsetDateTime,
)

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

    override fun getCountEachDay(nameId: Long, numOfDays: Int): List<BigInteger> {
        val correctEvents = events.values.filter { it.name.id == nameId }
        val groupByDate = correctEvents.groupBy { it.createdAt.toLocalDate() }
        val sortAndLimit = groupByDate.toList().sortedByDescending { it.first }.take(numOfDays)

//        val endDate = LocalDate.now()V
//        val startDate = endDate.minusDays(numOfDays.toLong())
//
//        val correctEvents = events.values.filter { it.name.id == nameId }
//        val groupByDate = correctEvents.groupBy { it.createdAt.toLocalDate() }

        return (0 until numOfDays).map { dayOffset ->
            val date = LocalDate.now().minusDays(dayOffset.toLong())
            val index = sortAndLimit.indexOfFirst { it.first == date }
            if (index != -1) {
                BigInteger.valueOf(sortAndLimit[index].second.size.toLong())
            } else {
                BigInteger.valueOf(0)
            }
        }
    }

    override fun deleteAllByNameId(nameId: Long) {
        events.remove(nameId)
    }

    fun clear() {
        events.clear()
        id = 0L
    }
}