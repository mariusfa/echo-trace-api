package com.echotrace.echotrace.repository

import java.math.BigInteger

/**
 * Main usage is for creating fake repository for testing.
 */
interface EventRepositoryInterface {

    fun insert(event: Event)

    fun count(nameId: Long): BigInteger

    fun deleteAllByNameId(nameId: Long)
}