package ru.itmocloudtechnologies.hiring.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.itmocloudtechnologies.hiring.model.Worker
import java.util.*

@Repository
interface WorkerRepository : CrudRepository<Worker, Int> {
    @Transactional
    fun deleteBySalary(salary: Float): Long
    @Transactional
    fun findAllBySalaryLessThan(salary: Float): List<Worker>
    @Transactional
    fun findFirstByOrderByStatusDesc(): Optional<Worker>

}