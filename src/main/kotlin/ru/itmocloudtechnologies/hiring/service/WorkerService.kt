package ru.itmocloudtechnologies.hiring.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.itmocloudtechnologies.hiring.model.Coordinates
import ru.itmocloudtechnologies.hiring.model.OrganizationType
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class WorkerService(
    private val workerRepository: WorkerRepository
) {

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    fun findAllPageable(pageable: Pageable): Page<Worker> =
        workerRepository.findAll(pageable)

    fun findAllWithFilter(
        filterField: String,
        filterValue: String,
        pageable: Pageable
    ): Page<Worker> {
        return when (filterField) {
            "name" -> workerRepository.findByName(filterValue, pageable)
            "salary" -> workerRepository.findBySalary(filterValue.toFloat(), pageable)
            "id" -> workerRepository.findById(filterValue.toInt(), pageable)
            "organizationType" -> workerRepository.findByOrganizationType(
                OrganizationType.valueOf(filterValue), pageable
            )
            "position" -> workerRepository.findByPosition(Position.valueOf(filterValue), pageable)
            "creationDate" -> workerRepository.findByCreationDate(
                LocalDate.from(formatter.parse(filterValue)), pageable
            )
            "coordinates" -> TODO()
            else -> throw IllegalArgumentException("Field $filterField does not exist")
        }
    }

    fun findById(id: Int): Optional<Worker> =
        workerRepository.findById(id)

    fun save(worker: Worker): Worker = workerRepository.save(worker)

    fun delete(id: Int) {
        workerRepository.deleteById(id)
    }

    fun deleteAll() {
        workerRepository.deleteAll()
    }

    fun modify(id: Int, worker: Worker): Optional<Worker> =
        workerRepository.findById(id)
            .map {
                it.apply {
                    name = worker.name ?: name
                    coordinates?.apply {
                        x = worker.coordinates?.x ?: x
                        y = worker.coordinates?.y ?: y
                    }
                    salary = worker.salary ?: salary
                    position = worker.position ?: position
                    status = worker.status ?: status
                    organizationType = worker.organizationType ?: organizationType
                }

                workerRepository.save(it)
            }

}