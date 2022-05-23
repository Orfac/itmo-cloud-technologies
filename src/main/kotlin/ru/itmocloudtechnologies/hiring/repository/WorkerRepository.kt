package ru.itmocloudtechnologies.hiring.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmocloudtechnologies.hiring.model.Worker
import java.util.*

@Repository
interface WorkerRepository : JpaRepository<Worker, Int> {

    fun deleteBySalary(salary: Float): Long
    fun findAllBySalaryLessThan(salary: Float): List<Worker>
    fun findFirstByOrderByStatus(): Optional<Worker>

}