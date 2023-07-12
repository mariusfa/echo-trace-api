package com.echotrace.echotrace.repository

interface UserRepositoryInterface {
    fun insert(user: User)
    fun getByName(name: String): User?
}