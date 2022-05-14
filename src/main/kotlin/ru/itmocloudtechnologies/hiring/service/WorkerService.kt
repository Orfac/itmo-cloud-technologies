package ru.itmocloudtechnologies.hiring.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository

@Service
class WorkerService {
    @Autowired
    lateinit var workerRepository: WorkerRepository;

    fun findAll(): List<Worker> {
        return workerRepository.findAll().toList()
    }

    fun save(worker: Worker) {
        workerRepository.save(worker)
    }

    fun deleteAll() {
        workerRepository.deleteAll()
    }
}