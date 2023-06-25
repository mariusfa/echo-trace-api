package com.echotrace.echotrace.repository


/**
 * Main usage is for creating fake repository for testing.
 */
interface NameRepositoryInterface {

    fun insert(name: Name)

    fun getNames(): List<Name>

    fun getByName(name: String): Name?

    fun update(name: Name)

    fun delete(id: Long)
}