package ru.itmocloudtechnologies.hiring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
open class HelloWorldController {

    init {
        println("asdJASDIJoqwoiejqoiwejqwoiejqiejowi")
        println("asdJASDIJoqwoiejqoiwejqwoiejqiejowi")
        println("asdJASDIJoqwoiejqoiwejqwoiejqiejowi")
        println("asdJASDIJoqwoiejqoiwejqwoiejqiejowid")
    }
    @GetMapping("/")
    fun hello(): String {
        return "Hello world from backend"
    }

}