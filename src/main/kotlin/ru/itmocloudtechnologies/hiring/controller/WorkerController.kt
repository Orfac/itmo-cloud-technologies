package ru.itmocloudtechnologies.hiring.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

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
    @GetMapping()
    fun getAllWorkersF(
        @RequestParam(defaultValue = 0.toString()) page: Int,
        @RequestParam(defaultValue = 5.toString()) size: Int,
        @RequestParam(defaultValue = "ASC") order: String,
        @RequestParam(defaultValue = "id") sortValue: String,
        // TODO Здесь должны быть не name а любые значения
        @RequestParam(required = false) name : String?
    ): Page<Worker> {
        val sort = Sort.by(Sort.Order(Sort.Direction.valueOf(order), sortValue))
        val pageable = PageRequest.of(page, size, sort)
        if (name == null){
            return workerService.findAllPageable(pageable)
        }
        return workerService.findAllWithFilter("name", name, pageable)
    }

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