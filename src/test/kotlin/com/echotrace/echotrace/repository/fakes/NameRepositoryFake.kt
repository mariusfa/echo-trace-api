package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.Name
import com.echotrace.echotrace.repository.NameRepositoryInterface
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class NameRepositoryFake() : NameRepositoryInterface {

    val names = mutableMapOf<Long, Name>()
    var id = 0L

    override fun insert(name: Name) {
        names[id] = Name(id, name.name)
        id++
    }

    override fun getNames(): List<Name> = names.values.toList()

    override fun getByName(name: String): Name? = names.values.firstOrNull { it.name == name }

    override fun update(name: Name) {
        names[name.id!!] = name
    }

    override fun delete(id: Long) {
        names.remove(id)
    }
}