package ru.itmocloudtechnologies.hiring.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.service.WorkerService

@RestController
@RequestMapping("workers")
class WorkerController {

    @Autowired lateinit var workerService: WorkerService

    @GetMapping
    fun getAllWorkers(): List<Worker> = workerService.findAll()
}