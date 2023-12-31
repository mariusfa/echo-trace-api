package com.echotrace.echotrace.repository.fakes

import com.echotrace.echotrace.repository.User
import com.echotrace.echotrace.repository.UserRepositoryInterface
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class UserRepositoryFake : UserRepositoryInterface {

    val users = mutableListOf<User>()
    var id = 0L

    override fun insert(user: User) {
        users.add(user.copy(id = id))
        id++
    }

    override fun getByName(name: String): User? = users.find { it.name == name }

    override fun findByApiToken(token: String): User? = users.find { it.apiToken == token }

    fun clear() {
        users.clear()
        id = 0L
    }
}