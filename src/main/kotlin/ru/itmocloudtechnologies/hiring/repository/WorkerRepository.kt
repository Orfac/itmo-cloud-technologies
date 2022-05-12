package ru.itmocloudtechnologies.hiring.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmocloudtechnologies.hiring.model.Worker

@Repository
interface WorkerRepository : JpaRepository<Worker, Int> {
}