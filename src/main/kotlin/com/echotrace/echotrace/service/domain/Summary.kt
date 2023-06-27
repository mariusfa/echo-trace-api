package com.echotrace.echotrace.service.domain

import java.math.BigInteger

data class Summary(
    val id: Long,
    val name: String,
    val count: BigInteger
)