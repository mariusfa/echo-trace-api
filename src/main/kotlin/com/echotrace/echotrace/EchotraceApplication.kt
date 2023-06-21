package com.echotrace.echotrace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EchotraceApplication

fun main(args: Array<String>) {
	runApplication<EchotraceApplication>(*args)
}
