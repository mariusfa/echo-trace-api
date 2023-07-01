package com.echotrace.echotrace.service.domain

import com.echotrace.echotrace.controller.SummaryDTO
import java.math.BigInteger

data class Summary(
    val id: Long,
    val name: String,
    val count: BigInteger
) {
    fun toDTO(): SummaryDTO = SummaryDTO(id = id, name = name, count = count)
}