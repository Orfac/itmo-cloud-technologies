package ru.itmocloudtechnologies.hiring.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmocloudtechnologies.hiring.dto.FilterWorkersRequest
import ru.itmocloudtechnologies.hiring.model.Coordinates
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Worker
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.criteria.Predicate

@Service
open class WorkerService {
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var workerRepository: WorkerRepository

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @PostConstruct
    fun post() {
        entityManager = entityManagerFactory.createEntityManager()
    }

    open fun getLessThanSalary(salary: Float): List<Worker> {
        return workerRepository.findAllBySalaryLessThan(salary)
    }


    open fun getWithSmallestStatus(): Optional<Worker> = workerRepository.findFirstByOrderByStatusDesc()

    @Transactional(readOnly = true)
    open fun findAll(
        filter: FilterWorkersRequest
    ): Page<Worker> {
        val em = entityManagerFactory.createEntityManager()

        val cb = em.criteriaBuilder

        var cq = cb.createQuery(Worker::class.java)

        val root = cq.from(Worker::class.java)

        val predicates = mutableListOf<Predicate>()

        filter.id?.let {
            predicates.add(
                cb.equal(
                    root.get<Long>("id"), it
                )
            )
        }

        filter.name?.let {
            predicates.add(
                cb.like(
                    root.get("name"), "%$it%"
                )
            )
        }

        filter.salary?.let {
            predicates.add(
                cb.le(
                    cb.abs(
                        cb.diff(
                            root.get("salary"), it
                        )
                    ), 1e-2
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
                cb.le(
                    cb.abs(
                        cb.diff(
                            root.get<Coordinates>("coordinates")
                                .get("x"), it
                        )
                    ), 1e-5
                )
            )
        }

        filter.coordinatesY?.let {
            predicates.add(
                cb.le(
                    cb.abs(
                        cb.diff(
                            root.get<Coordinates>("coordinates")
                                .get("y"), it
                        )
                    ), 1e-5
                )
            )
        }

        cq = cq.select(root).where(
            cb.and(
                *predicates.toTypedArray()
            )
        )

        val path = when (filter.sortedColumn) {
            "coordinatesX" -> root.get<Any>("coordinates").get("x")
            "coordinatesY" -> root.get<Any>("coordinates").get("y")
            else -> root.get<Any>(filter.sortedColumn)
        }

        cq = if (filter.sortedDirection == "ASC") {
            cq.orderBy(cb.asc(path))
        } else {
            cq.orderBy(cb.desc(path))
        }

        val query = em.createQuery(cq)
        val maxCount = query.resultList.size
        val pageable = PageRequest.of(filter.page, filter.pageSize)

        query.maxResults = filter.pageSize
        query.firstResult = filter.page * filter.pageSize

        return PageImpl(query.resultList, pageable, maxCount.toLong())
    }

    @Transactional
    open fun deleteBySalary(salary: Float): Long {
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