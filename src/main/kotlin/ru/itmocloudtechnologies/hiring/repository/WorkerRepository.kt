package ru.itmocloudtechnologies.hiring.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmocloudtechnologies.hiring.model.Coordinates
import ru.itmocloudtechnologies.hiring.model.OrganizationType
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Worker
import java.time.LocalDate

@Repository
interface WorkerRepository : JpaRepository<Worker, Int> {

    fun findByName(name: String, pageable: Pageable): Page<Worker>
    fun findById(id: Int, pageable: Pageable): Page<Worker>
    fun findBySalary(salary: Float, pageable: Pageable): Page<Worker>
    fun findByCoordinates(coordinates: Coordinates, pageable: Pageable): Page<Worker>
    fun findByCreationDate(creationDate: LocalDate, pageable: Pageable): Page<Worker>
    fun findByOrganizationType(organizationType: OrganizationType, pageable: Pageable): Page<Worker>
    fun findByPosition(position: Position, pageable: Pageable): Page<Worker>

}