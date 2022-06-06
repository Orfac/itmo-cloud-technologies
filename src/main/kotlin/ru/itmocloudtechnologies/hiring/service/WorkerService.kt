package ru.itmocloudtechnologies.hiring.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmocloudtechnologies.hiring.dto.FilterWorkersRequest
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

    open fun findAll(
        filter: FilterWorkersRequest
    ): Page<Worker> {

        val cb = entityManagerFactory.criteriaBuilder

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

//        filter.salary?.let {
//            predicates.add(
//                cb.equal(
//                    root.get<Float>("salary").`as`(String::class.java), it.toString()
//                )
//            )
//        }

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

//        filter.coordinatesX?.let {
//            predicates.add(
//                cb.equal(
//                    root.get<Coordinates>("coordinates")
//                        .get<Double>("x")
//                        .`as`(String::class.java), it.toString()
//                )
//            )
//        }
//
//        filter.coordinatesY?.let {
//            predicates.add(
//                cb.equal(
//                    root.get<Coordinates>("coordinates")
//                        .get<Double>("y")
//                        .`as`(String::class.java), it.toString()
//                )
//            )
//        }

        cq = cq.select(root).where(
            cb.and(
                *predicates.toTypedArray()
            )
        )


        val path = when (filter.sortedColumn) {
            "coordinateX" -> root.get<Any>("coordinate").get("x")
            "coordinateY" -> root.get<Any>("coordinate").get("y")
            else -> root.get<Any>(filter.sortedColumn)
        }

        cq = if (filter.sortedDirection == "ASC") {
            cq.orderBy(cb.asc(path))
        } else {
            cq.orderBy(cb.desc(path))
        }

        val query = entityManager.createQuery(cq)
        val maxCount = query.resultList.size
        val pageable = PageRequest.of(filter.page, filter.pageSize)

        query.maxResults = filter.pageSize
        query.firstResult = filter.page * filter.pageSize

        val result = query.resultList
            .filter {
                filter.salary == null
                        || it.salary.toString() == filter.salary.toString()
            }.filter {
                filter.coordinatesX == null ||
                        it.coordinates?.x.toString() == filter.coordinatesX.toString()
            }.filter {
                filter.coordinatesY == null ||
                        it.coordinates?.y.toString() == filter.coordinatesY.toString()
            }

        return PageImpl(result, pageable, maxCount.toLong())
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