package ru.itmocloudtechnologies.hiring.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.itmocloudtechnologies.hiring.dto.FilterWorkersRequest
import ru.itmocloudtechnologies.hiring.model.OrganizationType
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Status
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.service.WorkerService
import ru.itmocloudtechnologies.hiring.validation.group.CreateGroup

import javax.validation.ConstraintViolationException
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@RequestMapping("workers")
@Validated
class WorkerController(
    private val workerService: WorkerService
) {

    @GetMapping
    fun getAllWorkers(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) salary: Float?,
        @RequestParam(required = false) position: Position?,
        @RequestParam(required = false) status: Status?,
        @RequestParam(required = false) organizationType: OrganizationType?,
        @RequestParam(required = false) coordinatesX: Double?,
        @RequestParam(required = false) coordinatesY: Double?,
        @RequestParam(required = false, defaultValue = "ASC") sortDirection: String,
        @RequestParam(required = false, defaultValue = "id") sortedColumn: String,
        @RequestParam(required = false, defaultValue = 0.toString()) @PositiveOrZero pageNum: Int,
        @RequestParam(required = false, defaultValue = 5.toString()) @Positive pageSize: Int
    ): Page<Worker> {
        val filter = FilterWorkersRequest(
            name,
            coordinatesX,
            coordinatesY,
            salary,
            position,
            status,
            organizationType,
            sortDirection,
            sortedColumn,
            pageNum,
            pageSize
        )
        return workerService.findAll(filter)
    }

    @GetMapping("/lessThan")
    fun getLessThanSalary(
        @RequestParam salary: Float
    ): List<Worker> = workerService.getLessThanSalary(salary)

    @GetMapping("/withSmallestStatus")
    fun getWithSmallestStatus(): ResponseEntity<Worker> =
        workerService
            .getWithSmallestStatus()
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Worker> =
        workerService.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @DeleteMapping
    fun deleteMany(
        @RequestParam salary: Float
    ): Long = workerService.deleteBySalary(salary)

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