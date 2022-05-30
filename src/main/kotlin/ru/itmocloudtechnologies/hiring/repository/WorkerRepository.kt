package ru.itmocloudtechnologies.hiring.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.itmocloudtechnologies.hiring.model.Worker
import java.util.*
import javax.persistence.EntityManager

@Repository
interface WorkerRepository : CrudRepository<Worker, Int> {
    fun deleteBySalary(salary: Float): Long
    fun findAllBySalaryLessThan(salary: Float): List<Worker>
    fun findFirstByOrderByStatusDesc(): Optional<Worker>

}