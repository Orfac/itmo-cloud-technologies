package ru.itmocloudtechnologies.hiring.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.itmocloudtechnologies.hiring.dto.FilterWorkersRequest
import ru.itmocloudtechnologies.hiring.model.Coordinates
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate

@Service
class WorkerService(
    private val workerRepository: WorkerRepository,
    private val entityManager: EntityManager
) {

    fun getLessThanSalary(salary: Float): List<Worker> =
        workerRepository.findAllBySalaryLessThan(salary)

    fun getWithSmallestStatus(): Optional<Worker> = workerRepository.findFirstByOrderByStatus()

    fun findAll(
        filter: FilterWorkersRequest
    ): Page<Worker> {
        val cb = entityManager.criteriaBuilder

        var cq = cb.createQuery(Worker::class.java)

        val root = cq.from(Worker::class.java)

        val predicates = mutableListOf<Predicate>()

        filter.name?.let {
            predicates.add(
                cb.like(
                    root.get("name"), "%$it%"
                )
            )
        }

        filter.salary?.let {
            predicates.add(
                cb.equal(
                    root.get<Float>("salary"), it
                )
            )
        }

        filter.position?.let {
            predicates.add(
                cb.equal(
                    root.get<Position>("position"), it
                )
            )
        }

        filter.status?.let {
            predicates.add(
                cb.equal(
                    root.get<Position>("status"), it
                )
            )
        }

        filter.organizationType?.let {
            predicates.add(
                cb.equal(
                    root.get<Position>("organizationType"), it
                )
            )
        }

        filter.coordinatesX?.let {
            predicates.add(
                cb.equal(
                    root.get<Coordinates>("coordinates").get<Double>("x"), it
                )
            )
        }

        filter.coordinatesY?.let {
            predicates.add(
                cb.equal(
                    root.get<Coordinates>("coordinates").get<Double>("y"), it
                )
            )
        }

        cq = cq.select(root).where(
            cb.and(
                *predicates.toTypedArray()
            )
        )

        cq = if (filter.sortedDirection == "ASC") {
            cq.orderBy(cb.asc(root.get<Any>(filter.sortedColumn)))
        } else {
            cq.orderBy(cb.desc(root.get<Any>(filter.sortedColumn)))
        }

        val query = entityManager.createQuery(cq)
        val maxCount = query.resultList.size
        val pageable = PageRequest.of(filter.pageNum, filter.pageSize)

        query.maxResults = filter.pageSize
        query.firstResult = filter.pageNum * filter.pageSize

        return PageImpl(query.resultList, pageable, maxCount.toLong())
    }

    fun deleteBySalary(salary: Float): Long {
        return workerRepository.deleteBySalary(salary)
    }

    fun findById(id: Int): Optional<Worker> = workerRepository.findById(id)

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