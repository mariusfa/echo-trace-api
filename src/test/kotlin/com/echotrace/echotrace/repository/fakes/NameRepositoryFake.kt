package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.Name
import com.echotrace.echotrace.repository.NameRepositoryInterface
import com.echotrace.echotrace.repository.User
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class NameRepositoryFake() : NameRepositoryInterface {

    val names = mutableMapOf<Long, Name>()
    var id = 0L

    override fun insert(name: Name) {
        names[id] = Name(id, name.name, name.userId)
        id++
    }

    override fun getNames(user: User): List<Name> = names.values.toList().filter { it.userId == user.id }

    override fun getByName(name: String): Name? = names.values.firstOrNull { it.name == name }

    override fun update(name: Name) {
        names[name.id!!] = name
    }

    override fun delete(id: Long) {
        names.remove(id)
    }

    fun clear() {
        names.clear()
        id = 0L
    }
}