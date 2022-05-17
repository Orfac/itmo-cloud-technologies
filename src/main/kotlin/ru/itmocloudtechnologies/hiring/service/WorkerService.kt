package ru.itmocloudtechnologies.hiring.service

import org.springframework.stereotype.Service
import ru.itmocloudtechnologies.hiring.model.Coordinates
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository
import java.util.*

@Service
class WorkerService(
    private val workerRepository: WorkerRepository
) {

    fun findAll(): List<Worker> =
        workerRepository.findAll().toList()

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