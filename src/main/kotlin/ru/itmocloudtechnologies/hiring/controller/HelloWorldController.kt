package ru.itmocloudtechnologies.hiring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmocloudtechnologies.hiring.configuration.InitialWorkersUploader

@RestController
open class HelloWorldController {

    @GetMapping("/")
    fun hello(): String {
        return "Hello world from backend"
    }

}