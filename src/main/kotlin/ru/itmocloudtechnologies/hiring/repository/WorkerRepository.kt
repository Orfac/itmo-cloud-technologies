package ru.itmocloudtechnologies.hiring.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.itmocloudtechnologies.hiring.model.Worker

@Repository
interface WorkerRepository : CrudRepository<Worker, Int> {
}