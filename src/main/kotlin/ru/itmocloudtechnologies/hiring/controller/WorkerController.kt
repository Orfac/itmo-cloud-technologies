package ru.itmocloudtechnologies.hiring.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.service.WorkerService
import ru.itmocloudtechnologies.hiring.validation.group.CreateGroup
import javax.validation.ConstraintViolationException

@RestController
@RequestMapping("workers")
class WorkerController(
    val workerService: WorkerService
) {

    @GetMapping
    fun getAllWorkers(): List<Worker> = workerService.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Worker> =
        workerService.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody
        @Validated(CreateGroup::class)
        entity: Worker
    ): Worker = workerService.save(entity)

    @PutMapping("/{id}")
    fun modify(
        @PathVariable id: Int,

        @RequestBody
        @Validated
        entity: Worker
    ): ResponseEntity<Worker> = workerService.modify(id, entity)
        .map { ResponseEntity.ok(it) }
        .orElse(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int) {
        workerService.delete(id)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun validException(ex: ConstraintViolationException): Map<String, String> {
        val map = mutableMapOf<String, String>()

        ex.constraintViolations.forEach {
            map[it.propertyPath.toString()] = it.message
        }

        return map
    }

}