package com.userregister

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserRegisterApplication

fun main(args: Array<String>) {
    runApplication<UserRegisterApplication>(*args)
}
