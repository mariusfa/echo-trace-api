package com.echotrace.echotrace.repository


/**
 * Main usage is for creating fake repository for testing.
 */
interface NameRepositoryInterface {

    fun insert(name: Name)

    fun getNames(user: User): List<Name>

    fun getByName(name: String): Name?

    fun getById(id: Long, user: User): Name?

    fun update(name: Name)

    fun delete(id: Long)
}